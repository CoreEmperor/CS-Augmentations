package net.corespring.csaugmentations.Item;

import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SyringeGunInjectable extends DrinkableItem {
    public SyringeGunInjectable(Properties pProperties) {
        super(pProperties);
    }

    public abstract List<MobEffectInstance> getEffects();

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            getEffects().forEach(pLivingEntity::addEffect);

            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild) {
                ItemStack beaker = new ItemStack(CSItems.BEAKER.get());
                if (!player.getInventory().add(beaker)) {
                    player.drop(beaker, false);
                }
                pStack.shrink(1);
            }
        }
        return pStack;
    }
}
