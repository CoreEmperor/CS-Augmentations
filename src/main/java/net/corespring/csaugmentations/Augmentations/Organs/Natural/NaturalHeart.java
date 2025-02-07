package net.corespring.csaugmentations.Augmentations.Organs.Natural;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleHeart;
import net.corespring.csaugmentations.Utility.IOrganTiers;

public class NaturalHeart extends SimpleHeart {
    public NaturalHeart(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 0;
    }

}

