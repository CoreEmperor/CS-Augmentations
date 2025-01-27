package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Client.Overlays.ImmunosuppressantOverlay;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Registry.CSSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Immunosuppressant extends Item {
    public Immunosuppressant(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            pLivingEntity.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 12000, 0, false, false, true));
            pStack.shrink(1);

            ImmunosuppressantOverlay.remainingDisplayTicks = 100;

            pLevel.playSound(null, pLivingEntity.getBlockX(), pLivingEntity.getBlockY(), pLivingEntity.getBlockZ(), CSSoundEvents.IMMUNOSUPPRESSANT_USE.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if(!pAttacker.level().isClientSide && pTarget instanceof Player) {
            pTarget.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 12000, 0, false, false, true));
            pStack.shrink(1);
            pTarget.level().playSound(null, pTarget.getBlockX(), pTarget.getBlockY(), pTarget.getBlockZ(), CSSoundEvents.IMMUNOSUPPRESSANT_USE.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.immunosuppressant").withStyle(ChatFormatting.RED));
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.WOOL_BREAK;
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }
}
