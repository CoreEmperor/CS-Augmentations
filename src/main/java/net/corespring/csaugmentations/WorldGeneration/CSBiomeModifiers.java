package net.corespring.csaugmentations.WorldGeneration;

import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class CSBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_FOSSIL_ORE = registerKey("add_fossil_ore");
    public static final ResourceKey<BiomeModifier> ADD_SALT = registerKey("add_salt");
    public static final ResourceKey<BiomeModifier> ADD_CYCLOFUNGI = registerKey("add_cyclofungi");
    public static final ResourceKey<BiomeModifier> ADD_SOMNIFERUM = registerKey("add_somniferum");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_FOSSIL_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(CSPlacedFeatures.FOSSIL_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_SALT, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(Tags.Biomes.IS_WATER),
                HolderSet.direct(placedFeatures.getOrThrow(CSPlacedFeatures.SALT_PLACED_KEY)),
                GenerationStep.Decoration.RAW_GENERATION));

        context.register(ADD_CYCLOFUNGI, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.HAS_WOODLAND_MANSION),
                HolderSet.direct(placedFeatures.getOrThrow(CSPlacedFeatures.CYCLOFUNGI_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_SOMNIFERUM, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_RIVER),
                HolderSet.direct(placedFeatures.getOrThrow(CSPlacedFeatures.SOMNIFERUM_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(CSAugmentations.MOD_ID, name));
    }
}
