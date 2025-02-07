package net.corespring.csaugmentations.Augmentations.Organs.Cybernetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleJumper;
import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleWarper;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.entity.player.Player;

public class CyberJumper extends SimpleJumper {
    public CyberJumper(IOrganTiers pTier, Properties pProperties, double pJumpHeight, long pCooldown, float pExhaustion) {
        super(pTier, pProperties, pJumpHeight, pCooldown, pExhaustion);
    }

    @Override
    public int getOrganValue() {
        return 8;
    }

    @Override
    public void applyEffects(Player pPlayer) {
    }
}

