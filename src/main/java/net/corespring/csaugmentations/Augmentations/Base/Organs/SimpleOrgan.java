package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.IOrganTickable;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleOrgan extends Item implements IOrganTickable {
    private final CSOrganTiers pTier;
    private final Map<Attribute, Double> doubleAttributes = new HashMap<>();
    private final Map<Attribute, Integer> intAttributes = new HashMap<>();

    public SimpleOrgan(CSOrganTiers pTier, Properties pProperties) {
        super(pProperties);
        this.pTier = pTier;
        for (Attribute attribute : Attribute.values()) {
            if (pTier.getDoubleAttributes(attribute) != null) {
                doubleAttributes.put(attribute, pTier.getDoubleAttributes(attribute));
            }
            if (pTier.getIntAttributes(attribute) != null) {
                intAttributes.put(attribute, pTier.getIntAttributes(attribute));
            }
        }
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

    public double getDoubleAttribute(Attribute attribute) {
        return doubleAttributes.getOrDefault(attribute, 0.0);
    }

    public int getIntAttribute(Attribute attribute) {
        return intAttributes.getOrDefault(attribute, 0);
    }

    public boolean hasAttribute(Attribute attribute) {
        return doubleAttributes.containsKey(attribute) || intAttributes.containsKey(attribute);
    }
}
