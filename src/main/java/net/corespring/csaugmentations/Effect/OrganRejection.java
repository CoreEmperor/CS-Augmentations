package net.corespring.csaugmentations.Effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class OrganRejection extends MobEffect {
    public OrganRejection(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.level().isClientSide) {
            pLivingEntity.hurt(pLivingEntity.damageSources().wither(), 1.0f);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public int getColor() {
        return super.getColor();
    }


    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int i = 40 >> pAmplifier;
        if (i > 0) {
            return pDuration % i == 0;
        } else {
            return true;
        }
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return Collections.emptyList();
    }
}