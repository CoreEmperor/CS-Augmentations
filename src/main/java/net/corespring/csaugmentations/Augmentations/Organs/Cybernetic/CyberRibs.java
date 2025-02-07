package net.corespring.csaugmentations.Augmentations.Organs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleRibs;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberRibs extends SimpleRibs {
    public CyberRibs(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 6;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}

