package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.ILimbType;
import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleLeg extends SimpleOrgan implements ILimbType {
    public SimpleLeg(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public abstract void applyEffects(Player pPlayer);

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        double pGeneralArmor = getDoubleAttribute(CSOrganTiers.Attribute.GENERAL_ARMOR);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.crouch")
                .withStyle(ChatFormatting.GOLD));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pGeneralArmor)
                .append(Component.translatable("tooltip.csaugmentations.armor"))
                .withStyle(ChatFormatting.BLUE));
        double pSpeed = getDoubleAttribute(CSOrganTiers.Attribute.SPEED);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pSpeed)
                .append(Component.translatable("tooltip.csaugmentations.speed"))
                .withStyle(ChatFormatting.BLUE));
        double pFallDamageReduction = getDoubleAttribute(CSOrganTiers.Attribute.FALL_DAMAGE_REDUCTION);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + pFallDamageReduction)
                .append(Component.translatable("tooltip.csaugmentations.falldamage"))
                .withStyle(ChatFormatting.BLUE));
    }
}
