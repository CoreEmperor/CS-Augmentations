package net.corespring.csaugmentations.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class Silk extends MobEffect {

    public Silk(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public int getColor() {
        return super.getColor();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int interval = 40 >> pAmplifier;
        return interval <= 0 || pDuration % interval == 0;
    }
}
