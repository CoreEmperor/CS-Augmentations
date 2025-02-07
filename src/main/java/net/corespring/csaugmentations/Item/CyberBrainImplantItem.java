package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CyberBrainImplantItem extends Item {

    public CyberBrainImplantItem(Properties properties) {
        super(properties);
    }

    private static void applyNegativeEffects(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 400, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 400, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 400, 0, false, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 400, 0, false, false, true));
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
            OrganCap.OrganData organData = OrganCap.getOrganData(player);
            ItemStack brainStack = organData.getStackInSlot(CSAugUtil.OrganSlots.BRAIN);

            if (brainStack.getItem() == CSItems.NATURAL_BRAIN.get() && player.hasEffect(CSEffects.INCISION.get())) {
                organData.setStackInSlot(CSAugUtil.OrganSlots.BRAIN, new ItemStack(CSItems.CYBER_BRAIN.get()));
                organData.updateOrganData();

                applyNegativeEffects(player);

                if (!player.getAbilities().instabuild) {
                    pStack.shrink(1);
                }
            }
            pLevel.playSound(null, pLivingEntity.getBlockX(), pLivingEntity.getBlockY(), pLivingEntity.getBlockZ(), SoundEvents.SLIME_ATTACK, SoundSource.PLAYERS, 0.7F, 0.2F);
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        ItemStack cyberBrainItem = new ItemStack(CSItems.CYBER_BRAIN.get());
        List<Component> cyberBrainTooltip = cyberBrainItem.getTooltipLines(null, pIsAdvanced);

        if (!cyberBrainTooltip.isEmpty()) {
            pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.brain_implant").withStyle(ChatFormatting.GOLD));
            pTooltipComponents.add(cyberBrainTooltip.get(1));
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 20;
    }
}