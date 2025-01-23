package net.corespring.csaugmentations.Utility.Events;

import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.corespring.csaugmentations.Augmentations.Base.IOrganTickable;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.CSCommonConfigs;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Capability.OrganCapProvider;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.Network.CSNetwork;
import net.corespring.csaugmentations.Utility.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.PacketDistributor;

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
                        if (mixinEffectInstance.isEfficiencyApplied()) {
                            mixinEffectInstance.setDuration(effectInstance.getDuration());
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
                                CSOrganTiers tier = cap.getOrganTier(stack);
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
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(organData -> {
                    IItemHandlerModifiable itemHandler = new OrganCap.OrganData(16);
                    for (int i = 0; i < itemHandler.getSlots(); i++) {
                        ItemStack stack = itemHandler.getStackInSlot(i);
                        if (stack.getItem() instanceof IOrganTickable organ) {
                            organ.organTick(stack, player.level(), player, i);
                        }
                    }
                });
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

