package net.corespring.csaugmentations.Item.SurgicalEquipment;

import net.corespring.csaugmentations.Item.SurgicalToolItem;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Scalpel extends SurgicalToolItem {
    public Scalpel(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            pPlayer.startUsingItem(pUsedHand);
        }
        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration == 1) {
            pLivingEntity.getItemInHand(getHand()).hurtAndBreak(1, pLivingEntity, (livingEntity -> {
            }));
            pLivingEntity.addEffect(new MobEffectInstance(CSEffects.INCISION.get(), 6000, 0, false, false, true));
            pLevel.playLocalSound(pLivingEntity.getBlockX(), pLivingEntity.getBlockY(), pLivingEntity.getBlockZ(), SoundEvents.SLIME_BLOCK_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F, false);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide && pTarget instanceof Player && pTarget.hasEffect(CSEffects.SILK.get())) {
            pAttacker.getItemInHand(getHand()).hurtAndBreak(1, pAttacker, livingEntity -> {
            });
            pTarget.addEffect(new MobEffectInstance(CSEffects.INCISION.get(), 6000, 0, false, false, true));
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
