package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleArm;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class NaturalArm extends SimpleArm {
    public NaturalArm(IOrganTiers pTier, Properties pProperties) {
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
