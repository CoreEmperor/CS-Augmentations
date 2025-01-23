package net.corespring.csaugmentations.DataGen;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.WorldGeneration.CSBiomeModifiers;
import net.corespring.csaugmentations.WorldGeneration.CSConfiguredFeatures;
import net.corespring.csaugmentations.WorldGeneration.CSPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CSWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, CSConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, CSPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, CSBiomeModifiers::bootstrap);

    public CSWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CSAugmentations.MOD_ID));
    }
}