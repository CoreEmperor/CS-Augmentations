package net.corespring.csaugmentations.Capability;

import com.mojang.logging.LogUtils;
import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.corespring.csaugmentations.Augmentations.Base.Organs.*;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Utility.Network.CSNetwork;
import net.corespring.csaugmentations.Utility.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.CSOrganClasses;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;

public class OrganCap {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Capability<OrganData> ORGAN_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static class OrganData extends ItemStackHandler {
        private static final Map<Player, Boolean> wasUnderwaterMap = new HashMap<>();
        private static final UUID CyberAttackDamage = UUID.fromString("3957c7ce-19e4-4a0c-9ecf-5d6651250b17");
        private static final UUID CyberAttackSpeed = UUID.fromString("f625d1e5-58e2-4bc6-b2f9-3726313797b0");
        private static final UUID CyberSpeed = UUID.fromString("046d8411-8f88-485e-a5a4-a9ce38da9cf3");
        private static final UUID CyberArmor = UUID.fromString("925baaf5-1e0d-4090-93c0-c9213d4d7a0f");
        private final CSOrganClasses organClasses = new CSOrganClasses();
        private Player player;
        public boolean initialized = false;
        private int humanityLimit;
        private int currentCyberwareValue = 0;
        private int previousCyberwareValue;

        public void pDefaultOrgans() {
            setStackInSlot(0, new ItemStack(CSItems.NATURAL_BRAIN.get()));
            setStackInSlot(1, new ItemStack(CSItems.NATURAL_EYES.get()));
            setStackInSlot(2, new ItemStack(CSItems.NATURAL_RIBS.get()));
            setStackInSlot(3, new ItemStack(CSItems.NATURAL_SPINE.get()));
            setStackInSlot(4, new ItemStack(CSItems.NATURAL_ARM.get()));
            setStackInSlot(5, new ItemStack(CSItems.NATURAL_ARM.get()));
            setStackInSlot(6, new ItemStack(CSItems.NATURAL_LEG.get()));
            setStackInSlot(7, new ItemStack(CSItems.NATURAL_LEG.get()));
            setStackInSlot(8, new ItemStack(CSItems.NATURAL_LUNGS.get()));
            setStackInSlot(9, new ItemStack(CSItems.NATURAL_KIDNEY.get()));
            setStackInSlot(10, new ItemStack(CSItems.NATURAL_KIDNEY.get()));
            setStackInSlot(11, new ItemStack(CSItems.NATURAL_LIVER.get()));
            setStackInSlot(12, new ItemStack(CSItems.NATURAL_HEART.get()));
            setStackInSlot(13, new ItemStack(CSItems.NATURAL_STOMACH.get()));
            setStackInSlot(14, ItemStack.EMPTY);
            setStackInSlot(15, ItemStack.EMPTY);
            updateOrganData();
        }

        public OrganData(int size) {
            super(size);
            this.humanityLimit = new Random().nextInt(91) + 10;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public void copy(OrganData data) {
            for (int i = 0; i < this.getSlots(); i++) {
                this.setStackInSlot(i, data.getStackInSlot(i).copy());
            }
            this.initialized = data.initialized;
            this.humanityLimit = data.humanityLimit;
            this.currentCyberwareValue = data.currentCyberwareValue;
            this.previousCyberwareValue = data.previousCyberwareValue;
            this.updateOrganData();
        }

        public void updatePersistentData() {
            if (player != null) {
                CompoundTag csAugmentationsData = player.getPersistentData().getCompound(CSAugmentations.MOD_ID);
                csAugmentationsData.put("organ_data", this.serializeNBT());
                csAugmentationsData.putBoolean("initialized", this.initialized);
                csAugmentationsData.putInt("humanityLimit", this.humanityLimit);
                csAugmentationsData.putInt("currentCyberwareValue", this.currentCyberwareValue);
                player.getPersistentData().put(CSAugmentations.MOD_ID, csAugmentationsData);
            }
        }

        public Class<? extends SimpleOrgan> getOrganClassForSlot(int slot) {
            return organClasses.getOrganClass(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            Class<? extends SimpleOrgan> organClass = getOrganClassForSlot(slot);
            return organClass != null && organClass.isInstance(stack.getItem());
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = super.serializeNBT();
            tag.putBoolean("initialized", this.initialized);
            tag.putInt("humanityLimit", this.humanityLimit);
            tag.putInt("currentCyberwareValue", this.currentCyberwareValue);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            this.initialized = nbt.getBoolean("initialized");
            this.humanityLimit = nbt.getInt("humanityLimit");
            this.currentCyberwareValue = nbt.getInt("currentCyberwareValue");
        }

        public CSOrganTiers getOrganTier(ItemStack stack) {
            if (stack.getItem() instanceof SimpleOrgan organ) {
                return organ.getTier();
            }
            return CSOrganTiers.REMOVED;
        }

        public boolean isPresent(int slot) {
            return !getStackInSlot(slot).isEmpty();
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            try {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                ItemStack result = super.insertItem(slot, stack, simulate);
                updateOrganData();
                return result;
            } catch (Exception e) {
                LOGGER.error("Failed to InsertItem: {}", e.getMessage());
                return stack;
            }
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            try {
                ItemStack result = super.extractItem(slot, amount, simulate);
                updateOrganData();
                return result;
            } catch (Exception e) {
                LOGGER.error("Failed to ExtractItem: {}", e.getMessage());
                return ItemStack.EMPTY;
            }
        }

        public void updateOrganData() {
            synchronizeInventory();
            applyCyberArmorAttributes();

            if (player instanceof ServerPlayer sPlayer && !sPlayer.level().isClientSide) {
                CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> sPlayer),
                        new S2CSyncDataPacket(this, sPlayer.getId()));
            }
        }

        private void synchronizeInventory() {
            for (int i = 0; i < getSlots(); i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack.getItem() instanceof SimpleOrgan) {
                    ((SimpleOrgan) stack.getItem()).applyEffects(player);
                }
            }

            this.currentCyberwareValue = calculateTotalCyberwareValue();

            if (this.currentCyberwareValue != previousCyberwareValue) {
                previousCyberwareValue = this.currentCyberwareValue;
            }
            updatePersistentData();
        }

        public void applyOrganRejection() {
            if (player != null && !player.hasEffect(CSEffects.SILK.get())) {
                switch (this.currentCyberwareValue) {
                    case 10:
                    case 25:
                    case 50:
                    default:
                        break;
                }
            }
        }

        public boolean isCyberpsycho() {
            return this.currentCyberwareValue > this.humanityLimit;
        }

        private int calculateTotalCyberwareValue() {
            int totalValue = 0;
            for (int i = 0; i < getSlots(); i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack.getItem() instanceof SimpleOrgan) {
                    totalValue += ((SimpleOrgan) stack.getItem()).getOrganValue();
                }
            }
            return totalValue;
        }

        public void setHumanityLimit(int humanityLimit) {
            this.humanityLimit = humanityLimit;
        }

        public int getHumanityLimit() {
            return humanityLimit;
        }

        public void organTick() {
            boolean isUnderWater = player.isUnderWater();
            boolean wasUnderWater = wasUnderwaterMap.getOrDefault(player, false);

            handleLungs(isUnderWater, wasUnderWater);
            wasUnderwaterMap.put(player, isUnderWater);

            applyKidneyBuffs();
            applyLiverBuffs();
            applyOrganRejection();

            if (!disableLimbCriteria()) {
                applyLegBuffs(CSAugUtil.legsEnabled);
                applyArmBuffs(CSAugUtil.armsEnabled);
            } else {
                disabledLimbs();
            }

            //Placeholder
            if(getStackInSlot(CSAugUtil.OrganSlots.HEART).isEmpty()) {
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0);
            }
        }

        private void handleLungs(boolean isUnderWater, boolean wasUnderWater) {
            ItemStack lungs = getStackInSlot(CSAugUtil.OrganSlots.LUNGS);
            if (lungs.getItem() instanceof SimpleLungs) {
                int additionalAirTime = ((SimpleLungs) lungs.getItem()).getAdditionalAirTime();
                if (isUnderWater && !wasUnderWater) {
                    player.setAirSupply(player.getAirSupply() + additionalAirTime);
                }
            }
        }

        private boolean disableLimbCriteria() {
            return isPlayerMissingBS();
        }

        private boolean isPlayerMissingBS() {
            return !isPresent(CSAugUtil.OrganSlots.BRAIN) || !isPresent(CSAugUtil.OrganSlots.SPINE);
        }

        private void disabledLimbs() {
            AttributeInstance pSpeed = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (pSpeed != null) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                pSpeed.removeModifier(CyberSpeed);
            }
            AttributeInstance pAttackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (pAttackDamage != null) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0);
                pAttackDamage.removeModifier(CyberAttackDamage);
            }
            AttributeInstance pAttackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (pAttackSpeed != null) {
                player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0);
                pAttackSpeed.removeModifier(CyberAttackSpeed);
            }
        }

        private boolean hasCyberBrain() {
            ItemStack brainStack = getStackInSlot(CSAugUtil.OrganSlots.BRAIN);
            if (!brainStack.isEmpty() && brainStack.getItem() instanceof SimpleOrgan organ) {
                return organ.getTier().isAboveOrEqual(CSOrganTiers.CYBERNETIC);
            }
            return false;
        }

        private boolean isTierAboveProsthetic() {
            int[] Slots = new int[]{
                    CSAugUtil.OrganSlots.RIGHT_ARM,
                    CSAugUtil.OrganSlots.LEFT_ARM,
                    CSAugUtil.OrganSlots.RIGHT_LEG,
                    CSAugUtil.OrganSlots.LEFT_LEG
            };

            for (int slot : Slots) {
                ItemStack stack = getStackInSlot(slot);
                CSOrganTiers tier = getOrganTier(stack);

                if (tier.isAboveOrEqual(CSOrganTiers.CYBERNETIC)) {
                    return true;
                }
            }
            return false;
        }

        public void applyLegBuffs(boolean enable) {
            if (player != null) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
                player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CyberSpeed);

                double totalSpeedBonus = 0.0;

                int[] legSlots = new int[]{
                        CSAugUtil.OrganSlots.RIGHT_LEG,
                        CSAugUtil.OrganSlots.LEFT_LEG
                };

                for (int slot : legSlots) {
                    ItemStack stack = getStackInSlot(slot);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    if (stack.getItem() instanceof SimpleOrgan organ) {

                        if (isTierAboveProsthetic() && hasCyberBrain()) {
                            if (enable) {
                                totalSpeedBonus += organ.getAttribute(CSOrganTiers.Attribute.SPEED);
                            } else {
                                totalSpeedBonus += (double) CSOrganTiers.NATURAL.getAttribute(CSOrganTiers.Attribute.SPEED);
                            }
                        } else {
                            totalSpeedBonus += organ.getAttribute(CSOrganTiers.Attribute.SPEED);
                        }
                    }
                }

                if (totalSpeedBonus > 0.0) {
                    player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(CyberSpeed, "CyberSpeed", totalSpeedBonus, AttributeModifier.Operation.ADDITION));
                }
            }
        }

        public void applyArmBuffs(boolean enable) {
            if (player != null) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0);
                player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0);
                player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CyberAttackDamage);
                player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(CyberAttackSpeed);

                double totalAttackBonus = 0.0f;
                double totalAttackSpeedBonus = 0.0f;

                int[] armSlots = new int[]{
                        CSAugUtil.OrganSlots.RIGHT_ARM,
                        CSAugUtil.OrganSlots.LEFT_ARM
                };

                for (int slot : armSlots) {
                    ItemStack stack = getStackInSlot(slot);
                    if (stack.isEmpty()) {
                        continue;
                    }
                    if (stack.getItem() instanceof SimpleOrgan organ) {

                        if (isTierAboveProsthetic() && hasCyberBrain()) {
                            if (enable) {
                                totalAttackBonus += organ.getAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE);
                                totalAttackSpeedBonus += organ.getAttribute(CSOrganTiers.Attribute.ATTACK_SPEED);
                            } else {
                                totalAttackBonus += (double) CSOrganTiers.NATURAL.getAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE);
                                totalAttackSpeedBonus += (double) CSOrganTiers.NATURAL.getAttribute(CSOrganTiers.Attribute.ATTACK_SPEED);
                            }
                        }
                        totalAttackBonus += organ.getAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE);
                        totalAttackSpeedBonus += organ.getAttribute(CSOrganTiers.Attribute.ATTACK_SPEED);
                    }
                }

                if (totalAttackBonus > 0.0) {
                    player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(CyberAttackDamage, "CyberAttackDamage", totalAttackBonus, AttributeModifier.Operation.ADDITION));
                }

                if (totalAttackSpeedBonus > 0.0) {
                    player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(CyberAttackSpeed, "CyberAttackSpeed", totalAttackSpeedBonus, AttributeModifier.Operation.ADDITION));
                }
            }
        }

        private void applyKidneyBuffs() {
            double combinedEfficiency = calculateCombinedEfficiency(
                    new int[]{CSAugUtil.OrganSlots.LEFT_KIDNEY, CSAugUtil.OrganSlots.RIGHT_KIDNEY}, CSOrganTiers.Attribute.KIDNEY_EFFICIENCY);

            List<MobEffectInstance> kidneyEffectsToAdd = new ArrayList<>();
            for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;

                if (isHarmfulEffect(effectInstance) && !mixinEffectInstance.isEfficiencyApplied()) {
                    adjustEffectDuration(effectInstance, combinedEfficiency);
                    mixinEffectInstance.setEfficiencyApplied(true);
                }

                if (!player.hasEffect(effectInstance.getEffect())) {
                    mixinEffectInstance.setEfficiencyApplied(false);
                }

                if (combinedEfficiency == 0.0) {
                    kidneyEffectsToAdd.add(new MobEffectInstance(CSEffects.KidneyFailure.get(), 40, 0, false, false, true));
                }
            }
            addRemoveEffects(kidneyEffectsToAdd, new ArrayList<>());
        }

        private void applyLiverBuffs() {
            double combinedEfficiency = calculateCombinedEfficiency(
                    new int[]{CSAugUtil.OrganSlots.LIVER}, CSOrganTiers.Attribute.LIVER_EFFICIENCY);

            List<MobEffectInstance> liverEffectsToAdd = new ArrayList<>();
            for (MobEffectInstance effectInstance : new ArrayList<>(player.getActiveEffects())) {
                IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;

                if (!isHarmfulEffect(effectInstance) && !mixinEffectInstance.isEfficiencyApplied()) {
                    adjustEffectDurationAndTier(effectInstance, combinedEfficiency);
                    mixinEffectInstance.setEfficiencyApplied(true);
                }

                if (!player.hasEffect(effectInstance.getEffect())) {
                    mixinEffectInstance.setEfficiencyApplied(false);
                }

                if (combinedEfficiency == 0.0) {
                    liverEffectsToAdd.add(new MobEffectInstance(CSEffects.LiverFailure.get(), 40, 0, false, false, true));
                }
            }
            addRemoveEffects(liverEffectsToAdd, new ArrayList<>());
        }

        private boolean isHarmfulEffect(MobEffectInstance effectInstance) {
            MobEffectCategory category = effectInstance.getEffect().isBeneficial() ? MobEffectCategory.BENEFICIAL : MobEffectCategory.HARMFUL;
            return category == MobEffectCategory.HARMFUL;
        }

        private void addRemoveEffects(List<MobEffectInstance> effectsToAdd, List<MobEffectInstance> effectsToRemove) {
            effectsToAdd.forEach(player::addEffect);
            effectsToRemove.forEach(effect -> player.removeEffect(effect.getEffect()));
        }

        private void adjustEffectDuration(MobEffectInstance effectInstance, double combinedEfficiency) {
            IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
            int duration = mixinEffectInstance.getDuration();
            float multiplier = combinedEfficiency > 1.0 ? 1.0f / (float) combinedEfficiency : 1.0f + (1.0f - (float) combinedEfficiency);
            mixinEffectInstance.setDuration((int) (duration * multiplier));
        }

        private void adjustEffectDurationAndTier(MobEffectInstance effectInstance, double combinedEfficiency) {
            IMixinMobEffectInstance mixinEffectInstance = (IMixinMobEffectInstance) effectInstance;
            int duration = mixinEffectInstance.getDuration();
            float multiplier = combinedEfficiency > 1.0 ? 1.0f + (float) (combinedEfficiency - 1.0) : 1.0f - (1.0f - (float) combinedEfficiency);
            mixinEffectInstance.setDuration((int) (duration * multiplier));

            int additionalTiers = (int) ((combinedEfficiency - 1.0) / 0.5);
            mixinEffectInstance.setAmplifier(mixinEffectInstance.getAmplifier() + additionalTiers);
        }

        private double calculateCombinedEfficiency(int[] slots, CSOrganTiers.Attribute attribute) {
            return Arrays.stream(slots)
                    .mapToDouble(slot -> {
                        ItemStack stack = getStackInSlot(slot);
                        return !stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ ? organ.getAttribute(attribute) : 0.0;
                    }).sum();
        }

        public void applyStomachBuffs() {
            ItemStack stack = getStackInSlot(CSAugUtil.OrganSlots.STOMACH);
            int pHunger = 0;
            double pSat = 0.0;

            if (stack.getItem() instanceof SimpleOrgan organ) {
                pHunger += (int) organ.getAttribute(CSOrganTiers.Attribute.STOMACH_HUNGER);
                pSat += organ.getAttribute(CSOrganTiers.Attribute.STOMACH_SAT);
            } else if (stack.isEmpty()) {
                pHunger += (int) CSOrganTiers.REMOVED.getAttribute(CSOrganTiers.Attribute.STOMACH_HUNGER);
                pSat += (double) CSOrganTiers.REMOVED.getAttribute(CSOrganTiers.Attribute.STOMACH_SAT);
            }

            modifyPlayerFoodStats(pHunger, pSat);
        }

        private void modifyPlayerFoodStats(int hunger, double saturation) {
            FoodData foodStats = player.getFoodData();
            int newHunger = Math.max(0, foodStats.getFoodLevel() + hunger);
            float newSaturation = Math.max(0, foodStats.getSaturationLevel() + (float) saturation);

            foodStats.setFoodLevel(newHunger);
            foodStats.setSaturation(newSaturation);
        }

        private void applyCyberArmorAttributes() {
            if (player != null) {
                player.getAttribute(Attributes.ARMOR).removeModifier(CyberArmor);

                double totalArmor = 0.0;

                int[] armorSlots = new int[]{
                        CSAugUtil.OrganSlots.RIBS,
                        CSAugUtil.OrganSlots.SKIN,
                        CSAugUtil.OrganSlots.RIGHT_LEG,
                        CSAugUtil.OrganSlots.LEFT_LEG,
                        CSAugUtil.OrganSlots.RIGHT_ARM,
                        CSAugUtil.OrganSlots.LEFT_ARM
                };

                for (int slot : armorSlots) {
                    ItemStack stack = getStackInSlot(slot);
                    if (!stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ) {
                        double attributeValue = (slot == CSAugUtil.OrganSlots.RIBS)
                                ? organ.getAttribute(CSOrganTiers.Attribute.RIBS_ARMOR)
                                : organ.getAttribute(CSOrganTiers.Attribute.GENERAL_ARMOR);
                        totalArmor += attributeValue;
                    }
                }

                if (totalArmor > 0.0) {
                    player.getAttribute(Attributes.ARMOR).addTransientModifier(
                            new AttributeModifier(CyberArmor, "CyberArmor", totalArmor, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }

        public static OrganData getOrganData(Player player) {
            return player.getCapability(ORGAN_DATA).orElseGet(() -> {
                OrganData newData = new OrganData(16);
                newData.setPlayer(player);
                return newData;
            });
        }
    }

