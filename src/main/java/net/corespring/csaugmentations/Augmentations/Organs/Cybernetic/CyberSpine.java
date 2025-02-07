package net.corespring.csaugmentations.Augmentations.Organs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleSpine;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberSpine extends SimpleSpine {
    public CyberSpine(IOrganTiers pTier, Properties pProperties) {
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

