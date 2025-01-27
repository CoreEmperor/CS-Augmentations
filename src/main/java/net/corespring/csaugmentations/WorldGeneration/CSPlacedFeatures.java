package net.corespring.csaugmentations.WorldGeneration;

import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class CSPlacedFeatures {
    public static final ResourceKey<PlacedFeature> FOSSIL_ORE_PLACED_KEY = registerKey("fossil_ore_placed");
    public static final ResourceKey<PlacedFeature> SALT_PLACED_KEY = registerKey("salt_placed");
    public static final ResourceKey<PlacedFeature> CYCLOFUNGI_PLACED_KEY = registerKey("cyclofungi_placed");
    public static final ResourceKey<PlacedFeature> SOMNIFERUM_PLACED_KEY = registerKey("somniferum_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, FOSSIL_ORE_PLACED_KEY, configuredFeatures.getOrThrow(CSConfiguredFeatures.FOSSIL_ORE_KEY),
                CSOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(50))));

        register(context, SALT_PLACED_KEY, configuredFeatures.getOrThrow(CSConfiguredFeatures.SALT_KEY),
                CSOrePlacement.commonOrePlacement(2,
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(45), VerticalAnchor.absolute(65))));

        register(context, CYCLOFUNGI_PLACED_KEY, configuredFeatures.getOrThrow(CSConfiguredFeatures.CYCLOFUNGI_KEY),
                List.of(RarityFilter.onAverageOnceEvery(4), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

        register(context, SOMNIFERUM_PLACED_KEY, configuredFeatures.getOrThrow(CSConfiguredFeatures.SOMNIFERUM_KEY),
                List.of(RarityFilter.onAverageOnceEvery(6), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
    }


    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(CSAugmentations.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
