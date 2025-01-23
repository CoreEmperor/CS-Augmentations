package net.corespring.csaugmentations.DataGen;

import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), CSLootTableProvider.create(packOutput));

        generator.addProvider(event.includeClient(), new CSBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new CSItemModelProvider(packOutput, existingFileHelper));

        CSBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new CSBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        CSLibraryBlockTagGenerator LibraryblockTagGenerator = generator.addProvider(event.includeServer(),
                new CSLibraryBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new CSItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        generator.addProvider(event.includeServer(), new CSWorldGenProvider(packOutput, lookupProvider));

        generator.addProvider(event.includeServer(), new CSGlobalLootModifiersProvider(packOutput));
    }

}