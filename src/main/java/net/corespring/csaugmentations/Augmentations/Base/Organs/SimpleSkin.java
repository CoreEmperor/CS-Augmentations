package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleSkin extends SimpleOrgan {
    public SimpleSkin(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("")
                .append("" + getOrganValue())
                .append(Component.translatable("tooltip.csaugmentations.cost"))
                .withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getTier().getRibsArmor())
                .append(Component.translatable("tooltip.csaugmentations.armor"))
                .withStyle(ChatFormatting.BLUE));
    }
}
