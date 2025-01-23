package net.corespring.csaugmentations.Augmentations.Base.Limbs;

import net.corespring.csaugmentations.Augmentations.Base.ILimbType;
import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleOrgan;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleLeg extends SimpleOrgan implements ILimbType {
    private final CSOrganTiers pTier;

    public SimpleLeg(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
        this.pTier = pTier;
    }

    public CSOrganTiers getTier() {
        return pTier;
    }

    public abstract void applyEffects(Player pPlayer);

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        double generalArmor = getTier().getGeneralArmor() / 2;
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + generalArmor)
                .append(Component.translatable("tooltip.csaugmentations.armor"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getTier().getSpeed())
                .append(Component.translatable("tooltip.csaugmentations.speed"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.plus")
                .append("" + getTier().getFallDamageReduction())
                .append(Component.translatable("tooltip.csaugmentations.falldamage"))
                .withStyle(ChatFormatting.BLUE));
    }
}
