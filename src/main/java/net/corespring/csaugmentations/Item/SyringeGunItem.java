package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Capability.SyringeGunCap;
import net.corespring.csaugmentations.Client.Menus.SyringeGunMenu;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SyringeGunItem extends Item {
    public SyringeGunItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND) {
            pPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new SyringeGunMenu(id, inv),
                    Component.translatable("item.csaugmentations.syringe_gun")
            ));
        }
        return InteractionResultHolder.sidedSuccess(stack, pLevel.isClientSide());
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player pPlayer && !pPlayer.level().isClientSide) {
            pStack.getCapability(SyringeGunCap.SYRINGE_CAP).ifPresent(handler -> {
                ItemStack contained = handler.getStackInSlot(0);

                if (!contained.isEmpty() && contained.getItem() instanceof SyringeGunInjectable drinkable) {
                    drinkable.getEffects().forEach(effect -> {
                        pTarget.addEffect(new MobEffectInstance(
                                effect.getEffect(),
                                effect.getDuration(),
                                effect.getAmplifier(),
                                effect.isAmbient(),
                                effect.isVisible(),
                                effect.showIcon()
                        ));
                    });

                    handler.extractItem(0, 1, false);
                    giveBeakerToPlayer(pPlayer);
                    pPlayer.getCooldowns().addCooldown(this, 20);
                }
            });
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    private void giveBeakerToPlayer(Player pPlayer) {
        ItemStack beaker = new ItemStack(CSItems.BEAKER.get());
        if (!pPlayer.addItem(beaker)) {
            pPlayer.drop(beaker, false);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pStack.getCapability(SyringeGunCap.SYRINGE_CAP).ifPresent(handler -> {
            ItemStack contained = handler.getStackInSlot(0);
            if(!contained.isEmpty()) {
                pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.syringe_gun.contents")
                        .append(contained.getHoverName())
                        .withStyle(ChatFormatting.GRAY));
            }
        });
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if(tag == null) tag = new CompoundTag();
        CompoundTag finalTag = tag;
        stack.getCapability(SyringeGunCap.SYRINGE_CAP).ifPresent(handler -> {
            finalTag.put("SyringeData", handler.serializeNBT());
        });
        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag tag) {
        super.readShareTag(stack, tag);
        if(tag != null && tag.contains("SyringeData")) {
            stack.getCapability(SyringeGunCap.SYRINGE_CAP).ifPresent(handler -> {
                handler.deserializeNBT(tag.getCompound("SyringeData"));
            });
        }
    }
}

