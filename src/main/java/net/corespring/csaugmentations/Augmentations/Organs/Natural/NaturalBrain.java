package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleBrain;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class NaturalBrain extends SimpleBrain {
    public NaturalBrain(CSOrganTiers pTier, Properties pProperties) {
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

