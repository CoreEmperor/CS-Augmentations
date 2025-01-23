package net.corespring.csaugmentations.Augmentations.Base;

public interface IMixinMobEffectInstance {
    boolean isEfficiencyApplied();

    void setEfficiencyApplied(boolean applied);

    int getDuration();

    int getAmplifier();

    void setDuration(int duration);

    void setAmplifier(int amplifier);
}
