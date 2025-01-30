package net.corespring.csaugmentations.Augmentations.Limbs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Limbs.SimpleArm;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberArm extends SimpleArm {
    public CyberArm(CSOrganTiers pTier, Properties pProperties) {
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
