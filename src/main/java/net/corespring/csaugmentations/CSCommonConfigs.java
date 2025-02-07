package net.corespring.csaugmentations;

import net.minecraftforge.common.ForgeConfigSpec;

public class CSCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue CYBERNETICS_DROP_ON_DEATH;
    public static final ForgeConfigSpec.BooleanValue CYBERPSYCHOSIS_TOGGLE;
    public static final ForgeConfigSpec.BooleanValue ORGAN_REJECTION_TOGGLE;

    public static final ForgeConfigSpec.IntValue CYBERPSYCHOSIS_TRADING_THRESHOLD;
    public static final ForgeConfigSpec.LongValue CYBERPSYCHOSIS_MIN_INTERVAL;
    public static final ForgeConfigSpec.LongValue CYBERPSYCHOSIS_MAX_INTERVAL;
    public static final ForgeConfigSpec.LongValue CYBERPSYCHOSIS_SEVERITY_INCREASE_INTERVAL;
    public static final ForgeConfigSpec.LongValue CYBERPSYCHOSIS_TRADE_REFUSE_COOLDOWN;
    public static final ForgeConfigSpec.IntValue CYBERPSYCHOSIS_SEVERITY_REDUCTION_ON_SLEEP;
    public static final ForgeConfigSpec.DoubleValue CYBERPSYCHOSIS_SOUND_CHANCE;
    public static final ForgeConfigSpec.DoubleValue CYBERPSYCHOSIS_ACTIONBAR_CHANCE;
    public static final ForgeConfigSpec.DoubleValue CYBERPSYCHOSIS_FAKE_CHAT_CHANCE;
    public static final ForgeConfigSpec.DoubleValue CYBERPSYCHOSIS_PET_DEATH_CHANCE;

    static {
        BUILDER.push("Config for [CS] Augmentations");

        CYBERNETICS_DROP_ON_DEATH = BUILDER.comment("Do Cybernetics Drop on Death? (default: true)").define("dropCybernetics", true);

        CYBERPSYCHOSIS_TOGGLE = BUILDER.comment("Should Cyberpsychosis be active? (default: true)").define("togglePsychosis", true);

        ORGAN_REJECTION_TOGGLE = BUILDER.comment("Should Organ Rejection be active? (default: true)").define("toggleRejection", true);

        CYBERPSYCHOSIS_MIN_INTERVAL = BUILDER.comment("Minimum interval for cyberpsychosis events in ticks (default: 4800)").defineInRange("cyberpsychosisMinInterval", 4800L, 0L, Long.MAX_VALUE);
        CYBERPSYCHOSIS_MAX_INTERVAL = BUILDER.comment("Maximum interval for cyberpsychosis events in ticks (default: 168000)").defineInRange("cyberpsychosisMaxInterval", 168000L, 0L, Long.MAX_VALUE);
        CYBERPSYCHOSIS_SEVERITY_INCREASE_INTERVAL = BUILDER.comment("Interval in ticks for increasing cyberpsychosis severity (default: 1200)").defineInRange("cyberpsychosisSeverityIncreaseInterval", 1200L, 0L, Long.MAX_VALUE);
        CYBERPSYCHOSIS_TRADING_THRESHOLD = BUILDER.comment("The severity level threshold for villagers refusing to trade (default: 70)").defineInRange("cyberpsychosisTradingThreshold", 70, 0, 100);
        CYBERPSYCHOSIS_TRADE_REFUSE_COOLDOWN = BUILDER.comment("Cooldown in ticks for villagers refusing to trade due to cyberpsychosis (default: 6000)").defineInRange("cyberpsychosisTradeRefuseCooldown", 6000L, 0L, Long.MAX_VALUE);
        CYBERPSYCHOSIS_SEVERITY_REDUCTION_ON_SLEEP = BUILDER.comment("Reduction in cyberpsychosis severity when sleeping (default: 10)").defineInRange("cyberpsychosisSeverityReductionOnSleep", 10, 0, Integer.MAX_VALUE);
        CYBERPSYCHOSIS_SOUND_CHANCE = BUILDER.comment("Chance for cyberpsychosis sound event (default: 0.2)").defineInRange("cyberpsychosisSoundChance", 0.2, 0.0, 1.0);
        CYBERPSYCHOSIS_ACTIONBAR_CHANCE = BUILDER.comment("Chance for cyberpsychosis action bar event (default: 0.4)").defineInRange("cyberpsychosisActionBarChance", 0.4, 0.0, 1.0);
        CYBERPSYCHOSIS_FAKE_CHAT_CHANCE = BUILDER.comment("Chance for cyberpsychosis chat message event (default: 0.3)").defineInRange("cyberpsychosisChatChance", 0.3, 0.0, 1.0);
        CYBERPSYCHOSIS_PET_DEATH_CHANCE = BUILDER.comment("Chance for cyberpsychosis pet death event (default: 0.05)").defineInRange("cyberpsychosisPetDeathChance", 0.05, 0.0, 1.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
