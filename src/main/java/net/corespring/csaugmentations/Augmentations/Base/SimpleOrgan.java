package net.corespring.csaugmentations.Augmentations.Base;

import net.corespring.csaugmentations.Utility.CSOrganTiers.Attribute;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleOrgan extends Item implements IOrganTickable {
    private final IOrganTiers pTier;

    public SimpleOrgan(IOrganTiers pTier, Properties pProperties) {
        super(pProperties);
        this.pTier = pTier;
    }

    public IOrganTiers getTier() {
        return pTier;
    }

    public String getTierName() {
        return pTier.getName();
    }

    public abstract int getOrganValue();

    public abstract void applyEffects(Player pPlayer);

    public void organTick(ItemStack pStack, Level pLevel, Player pPlayer, int pSlotId) {
    }

    public double getDoubleAttribute(Attribute attribute) {
        return pTier.getDoubleAttribute(attribute);
    }

    public int getIntAttribute(Attribute attribute) {
        return pTier.getIntAttribute(attribute);
    }

    public boolean hasAttribute(Attribute attribute) {
        return pTier.getDoubleAttribute(attribute) != null || pTier.getIntAttribute(attribute) != null;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("")
                .append("" + getOrganValue())
                .append(Component.translatable("tooltip.csaugmentations.cost"))
                .withStyle(ChatFormatting.RED));
    }
}
