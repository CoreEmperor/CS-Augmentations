package net.corespring.csaugmentations.Augmentations.Limbs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleLeg;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberLeg extends SimpleLeg {

    public CyberLeg(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 8;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}
