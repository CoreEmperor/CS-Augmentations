package net.corespring.csaugmentations.mixin;

import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEffectInstance.class)
public abstract class MixinMobEffectInstance implements IMixinMobEffectInstance {
    private boolean efficiencyApplied = false;

    @Shadow
    private int duration;

    @Shadow
    private int amplifier;

    @Override
    public boolean isEfficiencyApplied() {
        return efficiencyApplied;
    }

    @Override
    public void setEfficiencyApplied(boolean applied) {
        this.efficiencyApplied = applied;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int getAmplifier() {
        return amplifier;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }
}
