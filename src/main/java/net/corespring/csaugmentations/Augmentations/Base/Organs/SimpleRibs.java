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

public abstract class SimpleRibs extends SimpleOrgan {
    public SimpleRibs(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        double pRibsArmor = getDoubleAttribute(CSOrganTiers.Attribute.RIBS_ARMOR) / 2;
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pRibsArmor)
                .append(Component.translatable("tooltip.csaugmentations.armor"))
                .withStyle(ChatFormatting.BLUE));
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
