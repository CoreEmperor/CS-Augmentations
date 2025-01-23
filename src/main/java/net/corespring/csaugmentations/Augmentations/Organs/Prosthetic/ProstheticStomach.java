package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleStomach;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class ProstheticStomach extends SimpleStomach {
    public ProstheticStomach(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 2;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}

