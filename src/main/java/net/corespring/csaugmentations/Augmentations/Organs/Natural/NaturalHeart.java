package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleHeart;
import net.corespring.csaugmentations.Registry.Utility.CSOrganTiers;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class NaturalHeart extends SimpleHeart {
    public NaturalHeart(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 0;
    }

    @Override
    public void applyEffects(Player pPlayer) {
        double tier = getTier().getHealth();
        pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tier);

    }


}

