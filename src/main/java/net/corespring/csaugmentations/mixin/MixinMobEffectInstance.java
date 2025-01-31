package net.corespring.csaugmentations.mixin;

import net.corespring.csaugmentations.Augmentations.Base.IMixinMobEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MobEffectInstance.class)
public abstract class MixinMobEffectInstance implements IMixinMobEffectInstance {
    @Unique
    private boolean cS_Augmentations$efficiencyApplied = false;

    @Shadow
    private int duration;

    @Shadow
    private int amplifier;

    @Override
    public boolean cS_Augmentations$isEfficiencyApplied() {
        return cS_Augmentations$efficiencyApplied;
    }

    @Override
    public void cS_Augmentations$setEfficiencyApplied(boolean applied) {
        this.cS_Augmentations$efficiencyApplied = applied;
    }

    @Override
    public int cS_Augmentations$getDuration() {
        return duration;
    }

    @Override
    public int cS_Augmentations$getAmplifier() {
        return amplifier;
    }

    @Override
    public void cS_Augmentations$setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void cS_Augmentations$setAmplifier(int amplifier) {
        this.amplifier = amplifier;
    }
}
