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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide() && entity instanceof Player player) {
            OrganCap.OrganData organData = OrganCap.getOrganData(player);

            int humanityLevel = organData.getHumanityLimit();
            int totalCyberwareValue = organData.calculateTotalCyberwareValue();

            player.sendSystemMessage(Component.translatable("neural_analyzer.csaugmentations.self_scan_humanity", humanityLevel));
            player.sendSystemMessage(Component.translatable("neural_analyzer.csaugmentations.self_scan_cyberware", totalCyberwareValue));
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide() && target instanceof Player) {
            OrganCap.OrganData targetData = OrganCap.getOrganData((Player) target);

            int targetHumanityLevel = targetData.getHumanityLimit();
            int targetCyberwareValue = targetData.calculateTotalCyberwareValue();

            player.sendSystemMessage(Component.translatable("neural_analyzer.csaugmentations.player_scan", target.getName().getString()));
            player.sendSystemMessage(Component.translatable("neural_analyzer.csaugmentations.player_scan_humanity", targetHumanityLevel));
            player.sendSystemMessage(Component.translatable("neural_analyzer.csaugmentations.player_scan_cyberware", targetCyberwareValue));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("tooltip.csaugmentations.neural_analyzer"));
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
