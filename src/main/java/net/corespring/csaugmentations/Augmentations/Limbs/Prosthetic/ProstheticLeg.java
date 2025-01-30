package net.corespring.csaugmentations.Augmentations.Limbs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleLeg;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class ProstheticLeg extends SimpleLeg {

    public ProstheticLeg(CSOrganTiers pTier, Properties pProperties) {
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
