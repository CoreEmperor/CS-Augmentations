package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleRibs;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class ProstheticRibs extends SimpleRibs {
    public ProstheticRibs(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 4;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }


}

