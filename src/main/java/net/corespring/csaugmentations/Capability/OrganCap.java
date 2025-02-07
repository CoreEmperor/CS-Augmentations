package net.corespring.csaugmentations.Capability;

import com.mojang.logging.LogUtils;
import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleLungs;
import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.CSCommonConfigs;
import net.corespring.csaugmentations.Network.CSNetwork;
import net.corespring.csaugmentations.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.CSOrganClasses;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class OrganCap {
    public static final Capability<OrganData> ORGAN_DATA = CapabilityManager.get(new CapabilityToken<>() {
    });
    private static final Logger LOGGER = LogUtils.getLogger();

    public static OrganData getOrganData(Player player) {
        return player.getCapability(ORGAN_DATA).orElseGet(() -> {
            OrganData newData = new OrganData(16);
            newData.setPlayer(player);
            return newData;
        });
    }

    public static class OrganData extends ItemStackHandler {
        private static final Map<Player, Boolean> wasUnderwaterMap = new HashMap<>();
        private static final UUID CyberAttackDamage = UUID.fromString("3957c7ce-19e4-4a0c-9ecf-5d6651250b17");
        private static final UUID CyberAttackSpeed = UUID.fromString("f625d1e5-58e2-4bc6-b2f9-3726313797b0");
        private static final UUID CyberSpeed = UUID.fromString("046d8411-8f88-485e-a5a4-a9ce38da9cf3");
        private static final UUID CyberArmor = UUID.fromString("925baaf5-1e0d-4090-93c0-c9213d4d7a0f");
        private final CSOrganClasses organClasses = new CSOrganClasses();
        public boolean initialized = false;
        private Player player;
        private int humanityLimit;
        private Cyberpsychosis cyberpsychosis;
        private int currentCyberwareValue = 0;
        private int previousCyberwareValue;

        public OrganData(int size) {
            super(size);
            this.humanityLimit = new Random().nextInt(91) + 10;
            this.cyberpsychosis = new Cyberpsychosis();
        }

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

        public void setPlayer(Player player) {
            this.player = player;
        }

        public void copy(OrganData data) {
            for (int i = 0; i < this.getSlots(); i++) {
                this.setStackInSlot(i, data.getStackInSlot(i).copy());
            }
            this.initialized = data.initialized;
            this.humanityLimit = data.humanityLimit;
            this.cyberpsychosis = data.cyberpsychosis;
            this.currentCyberwareValue = data.currentCyberwareValue;
            this.previousCyberwareValue = data.previousCyberwareValue;
            this.updateOrganData();
        }

        public void updatePersistentData() {
            if (player != null) {
                CompoundTag csAugmentationsData = player.getPersistentData().getCompound(CSAugmentations.MOD_ID);
                csAugmentationsData.put("organ_data", this.serializeNBT());
                csAugmentationsData.put("cyberpsychosis", this.cyberpsychosis.serializeNBT());
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
            tag.put("cyberpsychosis", this.cyberpsychosis.serializeNBT());
            tag.putInt("currentCyberwareValue", this.currentCyberwareValue);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            this.initialized = nbt.getBoolean("initialized");
            this.humanityLimit = nbt.getInt("humanityLimit");
            this.cyberpsychosis.deserializeNBT(nbt.getCompound("cyberpsychosis"));
            this.currentCyberwareValue = nbt.getInt("currentCyberwareValue");

        }

        public IOrganTiers getOrganTier(ItemStack stack) {
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
            if (player != null && !player.hasEffect(CSEffects.Immunosuppressant.get()) && CSCommonConfigs.ORGAN_REJECTION_TOGGLE.get()) {

                if (this.currentCyberwareValue >= 10 && this.currentCyberwareValue < 25) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 0, false, false, true));
                } else if (this.currentCyberwareValue >= 25 && this.currentCyberwareValue < 50) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 1, false, false, true));
                } else if (this.currentCyberwareValue >= 50 && this.currentCyberwareValue < 70) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 2, false, false, true));
                } else if (this.currentCyberwareValue >= 70 && this.currentCyberwareValue < 100) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 3, false, false, true));
                } else if (this.currentCyberwareValue >= 100 && this.currentCyberwareValue < 125) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 4, false, false, true));
                } else if (this.currentCyberwareValue >= 125) {
                    player.addEffect(new MobEffectInstance(CSEffects.ORGAN_REJECTION.get(), 40, 5, false, false, true));
                }
            }
        }

        public boolean isCyberpsycho() {
            return this.currentCyberwareValue > this.humanityLimit;
        }

        public Cyberpsychosis getCyberpsychosis() {
            if (cyberpsychosis == null) {
                if (CSCommonConfigs.CYBERPSYCHOSIS_TOGGLE.get()) {
                    cyberpsychosis = new Cyberpsychosis();
                }
            }
            return cyberpsychosis;
        }

        public int getHumanityLimit() {
            return humanityLimit;
        }

        public void setHumanityLimit(int humanityLimit) {
            this.humanityLimit = humanityLimit;
        }

        public boolean shouldRefuseTrade() {
            if (isCyberpsycho()) {
                return cyberpsychosis.shouldRefuseTrade();
            }
            return false;
        }

        public void organTick() {
            boolean isUnderWater = player.isUnderWater();
            boolean wasUnderWater = wasUnderwaterMap.getOrDefault(player, false);

            handleLungs(isUnderWater, wasUnderWater);
            wasUnderwaterMap.put(player, isUnderWater);
            applyEffects(CSAugUtil.armsEnabled, CSAugUtil.legsEnabled);
            applyOrganRejection();

            if (!disableLimbCriteria()) {
                applyEffects(CSAugUtil.armsEnabled, CSAugUtil.legsEnabled);
            } else {
                disabledLimbs();
            }

            // Placeholder
            if (getStackInSlot(CSAugUtil.OrganSlots.HEART).isEmpty()) {
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(0);
            }
        }

        private boolean disableLimbCriteria() {
            return isPlayerMissingNervousSystem();
        }

        private boolean isPlayerMissingNervousSystem() {
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

        public void applyEffects(boolean armsEnabled, boolean legsEnabled) {
            if (player != null) {
                resetAttributes();

                double legBonuses = calculateLegBuffs(legsEnabled);
                double[] armBonuses = calculateArmBuffs(armsEnabled);
                double totalArmor = calculateArmorBuffs();

                applyLegBuffs(legBonuses);
                applyArmBuffs(armBonuses);
                applyArmorBuffs(totalArmor);
            }
        }

        private void resetAttributes() {
            player.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0);
            player.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(0);
            player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(CyberSpeed);
            player.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(CyberAttackDamage);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(CyberAttackSpeed);
            player.getAttribute(Attributes.ARMOR).removeModifier(CyberArmor);
        }

        private void applyLegBuffs(double legBuffs) {
            if (legBuffs > 0.0) {
                player.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(new AttributeModifier(CyberSpeed, "CyberSpeed", legBuffs, AttributeModifier.Operation.ADDITION));
            }
        }

        private void applyArmBuffs(double[] armBuffs) {
            if (armBuffs[0] > 0.0) {
                player.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(new AttributeModifier(CyberAttackDamage, "CyberAttackDamage", armBuffs[0], AttributeModifier.Operation.ADDITION));
            }

            if (armBuffs[1] > 0.0) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(CyberAttackSpeed, "CyberAttackSpeed", armBuffs[1], AttributeModifier.Operation.ADDITION));
            }
        }

        public void applyStomachBuffs() {
            ItemStack stack = getStackInSlot(CSAugUtil.OrganSlots.STOMACH);
            int pHunger = 0;
            double pSat = 0.0;

            if (stack.getItem() instanceof SimpleOrgan organ) {
                IOrganTiers pTier = organ.getTier();
                pHunger += pTier.getIntAttribute(CSOrganTiers.Attribute.STOMACH_HUNGER);
                pSat += pTier.getDoubleAttribute(CSOrganTiers.Attribute.STOMACH_SAT);
            } else if (stack.isEmpty()) {
                IOrganTiers pRemovedTier = CSOrganTiers.REMOVED;
                pHunger += pRemovedTier.getIntAttribute(CSOrganTiers.Attribute.STOMACH_HUNGER);
                pSat += pRemovedTier.getDoubleAttribute(CSOrganTiers.Attribute.STOMACH_SAT);
            }

            modifyPlayerFoodStats(pHunger, pSat);
        }

        private void applyArmorBuffs(double totalArmor) {
            if (totalArmor > 0.0) {
                player.getAttribute(Attributes.ARMOR).addTransientModifier(
                        new AttributeModifier(CyberArmor, "CyberArmor", totalArmor, AttributeModifier.Operation.ADDITION));
            }
        }

        private boolean hasCyberBrain() {
            ItemStack brainStack = getStackInSlot(CSAugUtil.OrganSlots.BRAIN);
            if (!brainStack.isEmpty() && brainStack.getItem() instanceof SimpleOrgan organ) {
                IOrganTiers tier = organ.getTier();
                return tier.isAboveOrEqual(CSOrganTiers.CYBERNETIC);
            }
            return false;
        }

        public boolean isTierAboveProsthetic(int slot) {
            ItemStack stack = getStackInSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ) {
                IOrganTiers tier = organ.getTier();
                return tier.isAboveOrEqual(CSOrganTiers.CYBERNETIC);
            }
            return false;
        }

        private double calculateLegBuffs(boolean legsEnabled) {
            double totalSpeedBonus = 0.0;
            int[] legSlots = new int[]{CSAugUtil.OrganSlots.RIGHT_LEG, CSAugUtil.OrganSlots.LEFT_LEG};

            for (int slot : legSlots) {
                ItemStack stack = getStackInSlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ && organ.hasAttribute(CSOrganTiers.Attribute.SPEED)) {
                    IOrganTiers tier = organ.getTier();
                    if (isTierAboveProsthetic(slot) && hasCyberBrain()) {
                        totalSpeedBonus += legsEnabled
                                ? tier.getDoubleAttribute(CSOrganTiers.Attribute.SPEED)
                                : CSOrganTiers.NATURAL.getDoubleAttribute(CSOrganTiers.Attribute.SPEED);
                    } else {
                        totalSpeedBonus += tier.getDoubleAttribute(CSOrganTiers.Attribute.SPEED);
                    }
                }
            }

            return totalSpeedBonus;
        }

        private double[] calculateArmBuffs(boolean armsEnabled) {
            double totalAttackBonus = 0.0;
            double totalAttackSpeedBonus = 0.0;
            int[] armSlots = new int[]{CSAugUtil.OrganSlots.RIGHT_ARM, CSAugUtil.OrganSlots.LEFT_ARM};

            for (int slot : armSlots) {
                ItemStack stack = getStackInSlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ) {
                    IOrganTiers tier = organ.getTier();
                    if (isTierAboveProsthetic(slot) && hasCyberBrain()) {
                        if (organ.hasAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE)) {
                            totalAttackBonus += armsEnabled
                                    ? tier.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE)
                                    : CSOrganTiers.NATURAL.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE);
                        }
                        if (organ.hasAttribute(CSOrganTiers.Attribute.ATTACK_SPEED)) {
                            totalAttackSpeedBonus += armsEnabled
                                    ? tier.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_SPEED)
                                    : CSOrganTiers.NATURAL.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_SPEED);
                        }
                    } else {
                        if (organ.hasAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE)) {
                            totalAttackBonus += tier.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_DAMAGE);
                        }
                        if (organ.hasAttribute(CSOrganTiers.Attribute.ATTACK_SPEED)) {
                            totalAttackSpeedBonus += tier.getDoubleAttribute(CSOrganTiers.Attribute.ATTACK_SPEED);
                        }
                    }
                }
            }

            return new double[]{totalAttackBonus, totalAttackSpeedBonus};
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

        private double calculateArmorBuffs() {
            double totalArmor = 0.0;
            int[] armorSlots = new int[]{CSAugUtil.OrganSlots.RIBS, CSAugUtil.OrganSlots.SKIN, CSAugUtil.OrganSlots.RIGHT_LEG,
                    CSAugUtil.OrganSlots.LEFT_LEG, CSAugUtil.OrganSlots.RIGHT_ARM, CSAugUtil.OrganSlots.LEFT_ARM};

            for (int slot : armorSlots) {
                ItemStack stack = getStackInSlot(slot);
                if (!stack.isEmpty() && stack.getItem() instanceof SimpleOrgan organ) {
                    IOrganTiers tier = organ.getTier();
                    double attributeValue = 0.0;
                    if (slot == CSAugUtil.OrganSlots.RIBS && organ.hasAttribute(CSOrganTiers.Attribute.RIBS_ARMOR)) {
                        attributeValue = tier.getDoubleAttribute(CSOrganTiers.Attribute.RIBS_ARMOR);
                    } else if (organ.hasAttribute(CSOrganTiers.Attribute.GENERAL_ARMOR)) {
                        attributeValue = tier.getDoubleAttribute(CSOrganTiers.Attribute.GENERAL_ARMOR);
                    }
                    totalArmor += attributeValue;
                }
            }

            return totalArmor;
        }

        public int calculateTotalCyberwareValue() {
            int totalValue = 0;
            for (int i = 0; i < getSlots(); i++) {
                ItemStack stack = getStackInSlot(i);
                if (stack.getItem() instanceof SimpleOrgan) {
                    totalValue += ((SimpleOrgan) stack.getItem()).getOrganValue();
                }
            }
            return totalValue;
        }

        private void modifyPlayerFoodStats(int hunger, double saturation) {
            FoodData foodStats = player.getFoodData();
            int newHunger = Math.max(0, foodStats.getFoodLevel() + hunger);
            float newSaturation = Math.max(0, foodStats.getSaturationLevel() + (float) saturation);

            foodStats.setFoodLevel(newHunger);
            foodStats.setSaturation(newSaturation);
        }
    }
}

