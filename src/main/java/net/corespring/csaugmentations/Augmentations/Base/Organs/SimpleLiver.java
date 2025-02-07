package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleLiver extends SimpleOrgan {
    public SimpleLiver(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public double getEfficiency() {
        return getDoubleAttribute(CSOrganTiers.Attribute.LIVER_EFFICIENCY);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getEfficiency())
                .append(Component.translatable("tooltip.csaugmentations.liver"))
                .withStyle(ChatFormatting.BLUE));
    }
}
