package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleKidney;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class ProstheticKidney extends SimpleKidney {
    public ProstheticKidney(CSOrganTiers pTier, Properties pProperties) {
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

