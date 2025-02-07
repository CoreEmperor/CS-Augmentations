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

public abstract class SimpleStomach extends SimpleOrgan {
    public SimpleStomach(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        int pStomachHung = getIntAttribute(CSOrganTiers.Attribute.STOMACH_HUNGER) / 2;
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pStomachHung)
                .append(Component.translatable("tooltip.csaugmentations.stomachhung"))
                .withStyle(ChatFormatting.BLUE));
        double pStomachSat = getDoubleAttribute(CSOrganTiers.Attribute.STOMACH_SAT);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pStomachSat)
                .append(Component.translatable("tooltip.csaugmentations.stomachsat"))
                .withStyle(ChatFormatting.BLUE));
    }
}
