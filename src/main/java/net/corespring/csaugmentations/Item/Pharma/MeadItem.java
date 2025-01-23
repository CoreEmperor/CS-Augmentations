package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AlcoholicBeverageItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MeadItem extends AlcoholicBeverageItem {

    public MeadItem(int tier, Properties pProperties) {
        super(tier, pProperties);
    }

    @Override
    protected MobEffectInstance[] getPositiveEffects() {
        return switch (getTier()) {
            case 0 -> new MobEffectInstance[]{
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600)
            };
            case 1 -> new MobEffectInstance[]{
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1)
            };
            case 2 -> new MobEffectInstance[]{
                    new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1800, 1)
            };
            default -> new MobEffectInstance[]{};
        };
    }

    @Override
    protected int getBaseDurationPerDrink() {
        return 1000 * getTier();
    }

    @Override
    protected int getBaseAmplifierPerDrink() {
        return 1;
    }
}
