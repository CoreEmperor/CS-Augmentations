package net.corespring.csaugmentations.Augmentations.Base;

public interface IMixinMobEffectInstance {
    boolean cS_Augmentations$isEfficiencyApplied();

    void cS_Augmentations$setEfficiencyApplied(boolean applied);

    int cS_Augmentations$getDuration();

    int cS_Augmentations$getAmplifier();

    void cS_Augmentations$setDuration(int duration);

    void cS_Augmentations$setAmplifier(int amplifier);
}
