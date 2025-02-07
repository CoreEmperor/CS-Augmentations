package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CSAugmentations.MOD_ID);

    public static final RegistryObject<SoundEvent> IMMUNOSUPPRESSANT_USE = registerSoundEvents("immunosuppressant_use");
    public static final RegistryObject<SoundEvent> FABRICATING = registerSoundEvents("fabricating");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CSAugmentations.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}