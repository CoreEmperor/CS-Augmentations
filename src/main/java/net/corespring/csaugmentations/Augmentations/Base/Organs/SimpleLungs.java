package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.IAirTime;
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

public abstract class SimpleLungs extends SimpleOrgan implements IAirTime {
    int pAirTime = getIntAttribute(CSOrganTiers.Attribute.AIR_TIME);

    public SimpleLungs(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        int lungCapacity = pAirTime / 30;
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + lungCapacity)
                .append(Component.translatable("tooltip.csaugmentations.lung"))
                .withStyle(ChatFormatting.BLUE));
    }

    @Override
    public int getAdditionalAirTime() {
        return pAirTime;
    }
}
