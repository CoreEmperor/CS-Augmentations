package net.corespring.csaugmentations.Events;

import com.google.common.util.concurrent.AtomicDouble;
import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.corespring.csaugmentations.Augmentations.Base.IOrganTickable;
import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.CSCommonConfigs;
import net.corespring.csaugmentations.Capability.Cyberpsychosis;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Capability.OrganCapProvider;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Network.CSNetwork;
import net.corespring.csaugmentations.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForgeEvents {
    @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class InventoryEvents {

        @SubscribeEvent
        public static void playerInventory(TickEvent.PlayerTickEvent event) {
            if (event.player.level().isClientSide()) return;
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;

                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    cap.organTick();
                    cap.updateOrganData();

                    player.getActiveEffects().forEach(effectInstance -> {
                        IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                        if (mixinEffectInstance.cS_Augmentations$isEfficiencyApplied()) {
                            mixinEffectInstance.cS_Augmentations$setDuration(effectInstance.getDuration());
                        }
                    });
                });
            }
        }

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    if (!cap.isPresent(CSAugUtil.OrganSlots.RIBS)) {
                        float originalDamage = event.getAmount();
                        event.setAmount(originalDamage * 2);
                    }
                });

                if (event.getEntity().hasEffect(CSEffects.SILK.get())) {
                    int amplifier = event.getEntity().getEffect(CSEffects.SILK.get()).getAmplifier();
                    event.setAmount(event.getAmount() * (1.0F - 0.20F * (amplifier + 1)));
                }

                if (event.getSource().is(DamageTypes.FALL) && player.isCrouching()) {
                    player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                        double fallDamageReductionFactor = 1.0;

                        int[] legSlots = new int[]{
                                CSAugUtil.OrganSlots.RIGHT_LEG,
                                CSAugUtil.OrganSlots.LEFT_LEG
                        };

                        for (int slot : legSlots) {
                            ItemStack stack = cap.getStackInSlot(slot);
                            if (!stack.isEmpty()) {
                                    IOrganTiers tier = cap.getOrganTier(stack);
                                    fallDamageReductionFactor += tier.getFallDamageReduction();
                            }
                        }

                        event.setAmount((float) (event.getAmount() / fallDamageReductionFactor));
                    });
                }
            }
        }


        @SubscribeEvent
        public static void onPlayerEat(LivingEntityUseItemEvent.Finish event) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(OrganCap.OrganData::applyStomachBuffs);
            }
        }


        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                cap.updateOrganData();
                if (!cap.initialized) {
                    cap.pDefaultOrgans();
                    cap.initialized = true;
                    cap.updatePersistentData();
                }
                CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(cap, player.getId()));
            });
        }

        @SubscribeEvent
        public static void onPlayerDeath(LivingDeathEvent event) {
            if (event.getEntity() instanceof Player player) {
                CSAugUtil.onDeath(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            ServerPlayer player = (ServerPlayer) event.getEntity();

            if(!CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                player.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 6000, 0, false, false, true));
            }

            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                if (CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                    cap.pDefaultOrgans();
                }
                cap.updateOrganData();
                CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(cap, player.getId()));
            });
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (!CSCommonConfigs.CYBERNETICS_DROP_ON_DEATH.get()) {
                Player oldPlayer = event.getOriginal();
                oldPlayer.reviveCaps();
                oldPlayer.getCapability(OrganCap.ORGAN_DATA).ifPresent(oldCap -> {
                    event.getEntity().getCapability(OrganCap.ORGAN_DATA).ifPresent(newCap -> {
                        newCap.copy(oldCap);
                        newCap.updateOrganData();
                    });
                });
                oldPlayer.invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Player player = event.player;
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    for (int i = 0; i < cap.getSlots(); i++) {
                        ItemStack stack = cap.getStackInSlot(i);
                        if (stack.getItem() instanceof IOrganTickable organ) {
                            organ.organTick(stack, player.level(), player, i);
                        }
                    }

                    if (CSCommonConfigs.CYBERPSYCHOSIS_TOGGLE.get()) {
                        if (cap.isCyberpsycho() && player instanceof ServerPlayer serverPlayer) {
                            Cyberpsychosis cyberpsychosis = cap.getCyberpsychosis();
                            cyberpsychosis.handleCyberpsychosis(serverPlayer);
                        }
                    }
                });
            }
        }

        @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
        public static class EffectHandler {

            @SubscribeEvent
            public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
                ServerPlayer player = (ServerPlayer) event.getEntity();
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                    cap.updateOrganData();
                    CSNetwork.NETWORK_CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new S2CSyncDataPacket(cap, player.getId())
                    );
                });
            }

            @SubscribeEvent
            public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
                if (event.phase == TickEvent.Phase.START) {
                    applyKidneyEffects(event.player);
                    applyLiverEffects(event.player);
                }
            }

            private static void applyKidneyEffects(Player player) {
                double combinedEfficiency = calculateKidneyBuffs(player);
                List<MobEffectInstance> kidneyEffectsToAdd = new ArrayList<>();

                for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {
                    IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;

                    if (isHarmfulEffect(effectInstance) && !mixinEffectInstance.cS_Augmentations$isEfficiencyApplied()) {
                        adjustEffectDuration(effectInstance, combinedEfficiency);
                        mixinEffectInstance.cS_Augmentations$setEfficiencyApplied(true);
                    }

                    if (!player.hasEffect(effectInstance.getEffect())) {
                        mixinEffectInstance.cS_Augmentations$setEfficiencyApplied(false);
                    }

                    if (combinedEfficiency == 0.0) {
                        kidneyEffectsToAdd.add(new MobEffectInstance(CSEffects.KIDNEY_FAILURE.get(), 40, 0, false, false, true));
                    }
                }
                addRemoveEffects(player, kidneyEffectsToAdd, new ArrayList<>());
            }

            private static void applyLiverEffects(Player player) {
                double pEfficiency = calculateLiverBuffs(player);
                List<MobEffectInstance> liverEffectsToAdd = new ArrayList<>();

                for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {
                    IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;

                    if (isBeneficialEffect(effectInstance) && !mixinEffectInstance.cS_Augmentations$isEfficiencyApplied()) {
                        adjustEffectDurationAndTier(effectInstance, pEfficiency);
                        mixinEffectInstance.cS_Augmentations$setEfficiencyApplied(true);
                    }

                    if (!player.hasEffect(effectInstance.getEffect())) {
                        mixinEffectInstance.cS_Augmentations$setEfficiencyApplied(false);
                    }

                    if (pEfficiency == 0.0) {
                        liverEffectsToAdd.add(new MobEffectInstance(CSEffects.LIVER_FAILURE.get(), 40, 0, false, false, true));
                    }
                }
                addRemoveEffects(player, liverEffectsToAdd, new ArrayList<>());
            }

            private static double calculateKidneyBuffs(Player player) {
                return calculateCombinedEfficiency(player, new int[]{CSAugUtil.OrganSlots.LEFT_KIDNEY, CSAugUtil.OrganSlots.RIGHT_KIDNEY}, CSOrganTiers.Attribute.KIDNEY_EFFICIENCY);
            }

            private static double calculateLiverBuffs(Player player) {
                return calculateCombinedEfficiency(player, new int[]{CSAugUtil.OrganSlots.LIVER}, CSOrganTiers.Attribute.LIVER_EFFICIENCY);
            }

            private static void adjustEffectDuration(MobEffectInstance effectInstance, double combinedEfficiency) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                int duration = mixinEffectInstance.cS_Augmentations$getDuration();
                float multiplier = combinedEfficiency > 1.0 ? 1.0f / (float) combinedEfficiency : 1.0f + (1.0f - (float) combinedEfficiency);
                mixinEffectInstance.cS_Augmentations$setDuration((int) (duration * multiplier));
            }

            private static void adjustEffectDurationAndTier(MobEffectInstance effectInstance, double combinedEfficiency) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
                int duration = mixinEffectInstance.cS_Augmentations$getDuration();
                float multiplier = combinedEfficiency > 1.0 ? 1.0f + (float) (combinedEfficiency - 1.0) : 1.0f - (1.0f - (float) combinedEfficiency);
                mixinEffectInstance.cS_Augmentations$setDuration((int) (duration * multiplier));

                int additionalTiers = (int) ((combinedEfficiency - 1.0) / 0.5);
                mixinEffectInstance.cS_Augmentations$setAmplifier(mixinEffectInstance.cS_Augmentations$getAmplifier() + additionalTiers);
            }

            private static double calculateCombinedEfficiency(Player player, int[] slots, CSOrganTiers.Attribute attribute) {
                AtomicDouble totalEfficiency = new AtomicDouble(0.0);

                Arrays.stream(slots).forEach(slot -> {
                    player.getCapability(OrganCap.ORGAN_DATA).ifPresent(organData -> {
                        ItemStack stack = organData.getStackInSlot(slot);

                        double efficiency = !stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ && organ.hasAttribute(attribute)
                                ? organ.getDoubleAttribute(attribute) : 0.0;

                        totalEfficiency.addAndGet(efficiency);
                    });
                });
                return totalEfficiency.get();
            }

            private static boolean isHarmfulEffect(MobEffectInstance effectInstance) {
                MobEffectCategory category = effectInstance.getEffect().getCategory();
                return category == MobEffectCategory.HARMFUL;
            }

            private static boolean isBeneficialEffect(MobEffectInstance effectInstance) {
                MobEffectCategory category = effectInstance.getEffect().getCategory();
                return category == MobEffectCategory.BENEFICIAL;
            }

            private static void addRemoveEffects(Player player, List<MobEffectInstance> effectsToAdd, List<MobEffectInstance> effectsToRemove) {
                effectsToAdd.forEach(player::addEffect);
                effectsToRemove.forEach(effect -> player.removeEffect(effect.getEffect()));
            }
        }

        @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
        public static class CapabilityHandler {

            @SubscribeEvent
            public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
                if (event.getObject() instanceof Player) {
                    event.addCapability(new ResourceLocation(CSAugmentations.MOD_ID, "organ_data"), new OrganCapProvider((Player) event.getObject()));
                }
            }

            @SubscribeEvent
            public static void registerCapabilities(RegisterCapabilitiesEvent event) {
                event.register(OrganCap.OrganData.class);
            }
        }
    }
}

