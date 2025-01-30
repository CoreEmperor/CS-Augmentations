package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleLiver;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class NaturalLiver extends SimpleLiver {
    public NaturalLiver(CSOrganTiers pTier, Properties pProperties) {
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

