package net.corespring.csaugmentations.Registry.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

import java.util.Random;
import net.minecraft.world.effect.MobEffects;

public class OrganRejection extends MobEffect {
    private final Random random = new Random();
    private int nauseaTickCounter = 0;
    private int nextNauseaTick = getRandomNauseaTick();

    public OrganRejection(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    //Currently really annoying to test, but probably works
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide) {
            switch(pAmplifier) {
                case 0:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, false, false, true));
                    break;
                case 1:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false, true));
                    break;
                case 2:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 0, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 0, false, false, true));
                    applyNauseaEffect(pLivingEntity, pAmplifier);
                    break;
                case 3:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, false, true));
                    applyNauseaEffect(pLivingEntity, pAmplifier);
                    break;
                case 4:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 1, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 80, 0, false, false, true));
                    pLivingEntity.hurt(pLivingEntity.damageSources().wither(), 0.5f);
                    applyNauseaEffect(pLivingEntity, pAmplifier);
                    break;
                case 5:
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, 2, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2, false, false, true));
                    pLivingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 80, 2, false, false, true));
                    pLivingEntity.hurt(pLivingEntity.damageSources().wither(), 1.0f);
                    applyNauseaEffect(pLivingEntity, pAmplifier);
                    break;
                default:
                    break;
            }
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    private void applyNauseaEffect(LivingEntity pLivingEntity, int pAmplifier) {
        if (nauseaTickCounter >= nextNauseaTick) {
            int nauseaDuration = 200 + (pAmplifier * 100);
            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, nauseaDuration, pAmplifier, false, false, true));
            nauseaTickCounter = 0;
            nextNauseaTick = getRandomNauseaTick();
        }

        nauseaTickCounter++;
    }

    private int getRandomNauseaTick() {
        return 12000 + random.nextInt(36000);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int i = 40 >> pAmplifier;
        return i <= 0 || pDuration % i == 0;
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }
}