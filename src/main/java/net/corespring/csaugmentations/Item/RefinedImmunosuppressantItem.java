package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Capability.Cyberpsychosis;
import net.corespring.csaugmentations.Capability.OrganCap;
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

public class RefinedImmunosuppressantItem extends Item {
    public RefinedImmunosuppressantItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide && pLivingEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 72000, 0, false, false, true));

            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                if (cap.isCyberpsycho()) {
                    Cyberpsychosis cyberpsychosis = cap.getCyberpsychosis();
                    cyberpsychosis.reduceSeverity(20);
                }
            });

            if (!player.getAbilities().instabuild) {
                pStack.shrink(1);
            }
        }

        if (pLevel.isClientSide) {
            ImmunosuppressantOverlay.remainingDisplayTicks = 100;
        }

        pLevel.playSound((Player) pLivingEntity, pLivingEntity.getBlockX(), pLivingEntity.getBlockY(), pLivingEntity.getBlockZ(), CSSoundEvents.IMMUNOSUPPRESSANT_USE.get(), SoundSource.PLAYERS, 0.5F, 0.4F);
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide && pTarget instanceof Player) {
            pTarget.addEffect(new MobEffectInstance(CSEffects.Immunosuppressant.get(), 72000, 0, false, false, true));
            pStack.shrink(1);
        }
        pTarget.level().playSound(null, pTarget.getBlockX(), pTarget.getBlockY(), pTarget.getBlockZ(), CSSoundEvents.IMMUNOSUPPRESSANT_USE.get(), SoundSource.PLAYERS, 0.5F, 0.4F);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.immunosuppressant").withStyle(ChatFormatting.RED));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.WOOL_BREAK;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 10;
    }
}