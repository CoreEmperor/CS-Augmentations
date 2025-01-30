package net.corespring.csaugmentations.Augmentations.Limbs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleArm;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class NaturalArm extends SimpleArm {
    public NaturalArm(CSOrganTiers pTier, Properties pProperties) {
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
