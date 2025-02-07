package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.ILimbType;
import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleArm extends SimpleOrgan implements ILimbType {
    public SimpleArm(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public abstract void applyEffects(Player pPlayer);

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        double generalArmor = getTier().getGeneralArmor() / 2;
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + generalArmor)
                .append(Component.translatable("tooltip.csaugmentations.armor"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getTier().getAttackDamage())
                .append(Component.translatable("tooltip.csaugmentations.attackdamage"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getTier().getAttackSpeed())
                .append(Component.translatable("tooltip.csaugmentations.attackspeed"))
                .withStyle(ChatFormatting.BLUE));
    }
}
