package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.IOrganTickable;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.CSOrganTiers.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class SimpleOrgan extends Item implements IOrganTickable {
    private final CSOrganTiers pTier;

    public SimpleOrgan(CSOrganTiers pTier, Properties pProperties) {
        super(pProperties);
        this.pTier = pTier;
    }

    public CSOrganTiers getTier() {
        return pTier;
    }

    public String getTierName() {
        return pTier.name();
    }

    public abstract int getOrganValue();

    public abstract void applyEffects(Player pPlayer);

    public void organTick(ItemStack pStack, Level pLevel, Player pPlayer, int pSlotId) {
    }

    public double getAttribute(CSOrganTiers.Attribute attribute) {
        Object attrValue = pTier.getAttribute(attribute);
        if (attrValue instanceof Number) {
            return ((Number) attrValue).doubleValue();
        } else {
            throw new IllegalArgumentException("Attribute value is not a number: " + attribute);
        }
    }
}
