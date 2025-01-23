package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Item.DrinkablePharmaItem;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AlcoholicBeverageItem extends DrinkablePharmaItem {
    private final int tier;

    public AlcoholicBeverageItem(int tier, Properties pProperties) {
        super(pProperties);
        this.tier = tier;
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            applyIntoxicationEffect(pLivingEntity);
            applyPositiveEffects(pLivingEntity);

            if (pLivingEntity instanceof Player player) {
                handleEmptyBottle(player);
                pStack.shrink(1);
            }
        }
        return pStack;
    }

    private void applyIntoxicationEffect(LivingEntity entity) {
        MobEffectInstance currentEffect = entity.getEffect(CSEffects.INTOXICATED.get());
        int amplifier = getBaseAmplifierPerDrink();
        int duration = getBaseDurationPerDrink();

        if (currentEffect != null) {
            amplifier = currentEffect.getAmplifier() + getBaseAmplifierPerDrink();
            duration = currentEffect.getDuration() + getBaseDurationPerDrink();

            if (amplifier > 7) {
                amplifier = 7;
            }
        }

        entity.addEffect(new MobEffectInstance(CSEffects.INTOXICATED.get(), duration, amplifier, false, false, true));
    }

    private void applyPositiveEffects(LivingEntity entity) {
        for (MobEffectInstance effect : getPositiveEffects()) {
            entity.addEffect(effect);
        }
    }

    private void handleEmptyBottle(Player player) {
        ItemStack bottle = new ItemStack(CSItems.ALCOHOL_BOTTLE.get());
        if (!player.getInventory().add(bottle)) {
            player.drop(bottle, false);
        }
    }

    protected abstract MobEffectInstance[] getPositiveEffects();

    protected abstract int getBaseDurationPerDrink();

    protected abstract int getBaseAmplifierPerDrink();

    public int getTier() {
        return tier;
    }
}
