package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.DrinkablePharmaItem;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SilkItem extends DrinkablePharmaItem {
    public SilkItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            pLivingEntity.addEffect(new MobEffectInstance(CSEffects.SILK.get(), 1000, 0, false, false, true));

            if (pLivingEntity instanceof Player player) {
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
