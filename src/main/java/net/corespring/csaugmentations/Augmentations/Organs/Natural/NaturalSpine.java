package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleSpine;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class NaturalSpine extends SimpleSpine {
    public NaturalSpine(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 0;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}

