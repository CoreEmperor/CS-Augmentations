package net.corespring.csaugmentations.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Intoxicated extends MobEffect {

    public Intoxicated(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        super.applyEffectTick(pLivingEntity, pAmplifier);

        if (!pLivingEntity.level().isClientSide) {
            double motionFactor = 0.04 + (0.01 * pAmplifier);
            double randomMotionX = (pLivingEntity.level().random.nextDouble() - 0.5) * motionFactor;
            double randomMotionZ = (pLivingEntity.level().random.nextDouble() - 0.5) * motionFactor;
                pLivingEntity.push(randomMotionX, 0, randomMotionZ);

                if(pLivingEntity instanceof Player player) {
                    player.hurtMarked = true;
                }

        }
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
