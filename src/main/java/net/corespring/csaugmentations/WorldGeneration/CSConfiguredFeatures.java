package net.corespring.csaugmentations.WorldGeneration;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class CSConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOSSIL_ORE_KEY = registerKey("fossil_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SALT_KEY = registerKey("salt_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SOMNIFERUM_KEY = registerKey("somniferum");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest sandReplaceable = new BlockMatchTest(Blocks.SAND);
        RuleTest gravelReplaceable = new BlockMatchTest(Blocks.GRAVEL);
        RuleTest netherrackReplacable = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endReplaceable = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> FOSSIL = List.of(
                OreConfiguration.target(stoneReplaceable, CSBlocks.FOSSIL_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, CSBlocks.DEEPSLATE_FOSSIL_ORE.get().defaultBlockState()));
        register(context, FOSSIL_ORE_KEY, Feature.ORE, new OreConfiguration(FOSSIL, 6));

        List<OreConfiguration.TargetBlockState> SALT = List.of(
                OreConfiguration.target(stoneReplaceable, CSBlocks.SALT.get().defaultBlockState()),
                OreConfiguration.target(sandReplaceable, CSBlocks.SALT.get().defaultBlockState()),
                OreConfiguration.target(gravelReplaceable, CSBlocks.SALT.get().defaultBlockState()));
        register(context, SALT_KEY, Feature.ORE, new OreConfiguration(SALT, 20));

        register(context, SOMNIFERUM_KEY, Feature.FLOWER,
                new RandomPatchConfiguration(19, 4, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(CSBlocks.WILD_SOMNIFERUM.get().defaultBlockState())))));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(CSAugmentations.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}

