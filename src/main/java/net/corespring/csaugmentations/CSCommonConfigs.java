package net.corespring.csaugmentations;

import net.minecraftforge.common.ForgeConfigSpec;

public class CSCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CYBERNETICS_DROP_ON_DEATH;

    static {
        BUILDER.push("Config for [CS] Augmentations");

        CYBERNETICS_DROP_ON_DEATH = BUILDER.comment("Do Cybernetics Drop on Death?")
                .define("dropCybernetics", true);


        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
