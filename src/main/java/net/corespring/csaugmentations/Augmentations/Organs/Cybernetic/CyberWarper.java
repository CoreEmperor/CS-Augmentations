package net.corespring.csaugmentations.Augmentations.Organs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleWarper;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberWarper extends SimpleWarper {
    public CyberWarper(IOrganTiers pTier, Properties pProperties, int pTeleportDistance, long pCooldown, float pExhaustion, int pChargeTime) {
        super(pTier, pProperties, pTeleportDistance, pCooldown, pExhaustion, pChargeTime);
    }

    @Override
    public int getOrganValue() {
        return 15;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}

