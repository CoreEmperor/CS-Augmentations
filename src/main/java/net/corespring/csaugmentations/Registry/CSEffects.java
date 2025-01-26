package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Effect.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSEffects {
    public static final DeferredRegister<MobEffect> Mob_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CSAugmentations.MOD_ID);

    public static Supplier<MobEffect> SILK =
            Mob_EFFECTS.register("silk",
                    () -> new Silk(MobEffectCategory.NEUTRAL, 0x561314));
    public static Supplier<MobEffect> INTOXICATED =
            Mob_EFFECTS.register("intoxicated",
                    () -> new Intoxicated(MobEffectCategory.NEUTRAL, 0x561314));

    public static Supplier<MobEffect> INCISION =
            Mob_EFFECTS.register("incision",
                    () -> new Incision(MobEffectCategory.NEUTRAL, 0x561314));
    public static Supplier<MobEffect> HEMOSTAT =
            Mob_EFFECTS.register("hemostat",
                    () -> new Hemostat(MobEffectCategory.NEUTRAL, 0x561314));
    public static Supplier<MobEffect> KidneyFailure =
            Mob_EFFECTS.register("kidneyfailure",
                    () -> new KidneyFailure(MobEffectCategory.NEUTRAL, 0x561314));
    public static Supplier<MobEffect> LiverFailure =
            Mob_EFFECTS.register("liverfailure",
                    () -> new LiverFailure(MobEffectCategory.NEUTRAL, 0x561314));

    public static Supplier<MobEffect> OrganRejection =
            Mob_EFFECTS.register("organrejection",
                    () -> new OrganRejection(MobEffectCategory.HARMFUL, 0x561314));
    public static Supplier<MobEffect> Immunosuppressant =
            Mob_EFFECTS.register("immunosuppressant",
                    () -> new ImmunoSuppressant(MobEffectCategory.NEUTRAL, 0x561314));


    public static void register(IEventBus eventBus) {
        Mob_EFFECTS.register(eventBus);
    }
}
