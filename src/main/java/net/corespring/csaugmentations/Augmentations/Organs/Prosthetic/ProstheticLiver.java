package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleLiver;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class ProstheticLiver extends SimpleLiver {
    public ProstheticLiver(IOrganTiers pTier, Properties pProperties) {
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

