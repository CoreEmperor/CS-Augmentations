package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NeuralAnalyzerItem extends Item {

    public NeuralAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide() && pLivingEntity instanceof Player player) {
            OrganCap.OrganData organData = OrganCap.getOrganData(player);

            int humanityLevel = organData.getHumanityLimit();
            int totalCyberwareValue = organData.calculateTotalCyberwareValue();

            player.sendSystemMessage(Component.translatable("message.csaugmentations.self_scan_humanity", humanityLevel));
            player.sendSystemMessage(Component.translatable("message.csaugmentations.self_scan_cyberware", totalCyberwareValue));
        }

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if (!pPlayer.level().isClientSide() && pInteractionTarget instanceof Player) {
            OrganCap.OrganData targetData = OrganCap.getOrganData((Player) pInteractionTarget);

            int targetHumanityLevel = targetData.getHumanityLimit();
            int targetCyberwareValue = targetData.calculateTotalCyberwareValue();

            pPlayer.sendSystemMessage(Component.translatable("message.csaugmentations.player_scan", pInteractionTarget.getName().getString()));
            pPlayer.sendSystemMessage(Component.translatable("message.csaugmentations.player_scan_humanity", targetHumanityLevel));
            pPlayer.sendSystemMessage(Component.translatable("message.csaugmentations.player_scan_cyberware", targetCyberwareValue));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.neural_analyzer"));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 20;
    }
}
