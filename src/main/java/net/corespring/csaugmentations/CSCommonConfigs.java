package net.corespring.csaugmentations;

import net.minecraftforge.common.ForgeConfigSpec;

public class CSCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CYBERNETICS_DROP_ON_DEATH;
    public static final ForgeConfigSpec.IntValue CYBERPSYCHOSIS_TRADING_THRESHOLD;

    static {
        BUILDER.push("Config for [CS] Augmentations");

        CYBERNETICS_DROP_ON_DEATH = BUILDER.comment("Do Cybernetics Drop on Death? (default: true)")
                .define("dropCybernetics", true);

        CYBERPSYCHOSIS_TRADING_THRESHOLD = BUILDER.comment("The severity level threshold for villagers refusing to trade (default: 70)")
                .defineInRange("cyberpsychosisTradingThreshold", 70, 0, 100);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
