package net.corespring.csaugmentations.DataGen;

import net.corespring.csaugmentations.Block.CSCropBlock;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;
import java.util.function.Supplier;

public class CSBlockStateProvider extends BlockStateProvider {
    public CSBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CSAugmentations.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(CSBlocks.INVISIBLE_CHEM);
        blockWithItem(CSBlocks.INVISIBLE_FAB);
        CustomHorizontalBlock(CSBlocks.CULTIVATOR.get(), "cultivator");
        CustomHorizontalLightBlock(CSBlocks.REFINERY.get(), "refinery");
        CustomHorizontalBlock(CSBlocks.CHEMISTRY_TABLE.get(), "chemistry_table");
        CustomHorizontalBlock(CSBlocks.FABRICATOR.get(), "fabricator");

        blockWithItem(CSBlocks.FOSSIL_ORE);
        blockWithItem(CSBlocks.DEEPSLATE_FOSSIL_ORE);
        blockWithItem(CSBlocks.FOSSIL_BLOCK);

        blockWithItem(CSBlocks.SALT);
        stairsBlock(((StairBlock) CSBlocks.SALT_STAIRS.get()), blockTexture(CSBlocks.SALT.get()));
        slabBlock(((SlabBlock) CSBlocks.SALT_SLAB.get()), blockTexture(CSBlocks.SALT.get()), blockTexture(CSBlocks.SALT.get()));
        blockWithItem(CSBlocks.POLISHED_SALT);
        stairsBlock(((StairBlock) CSBlocks.POLISHED_SALT_STAIRS.get()), blockTexture(CSBlocks.POLISHED_SALT.get()));
        slabBlock(((SlabBlock) CSBlocks.POLISHED_SALT_SLAB.get()), blockTexture(CSBlocks.POLISHED_SALT.get()), blockTexture(CSBlocks.POLISHED_SALT.get()));

        this.simpleBlockWithItem(CSBlocks.CYCLOFUNGI.get(),
                this.models().cross(this.blockTexture(CSBlocks.CYCLOFUNGI.get()).getPath(), this.blockTexture(CSBlocks.CYCLOFUNGI.get())).renderType("cutout"));
        
        blockWithItem(CSBlocks.BLOCK_SOMNIFERUM_SAP);
        simpleBlockWithItem(CSBlocks.WILD_SOMNIFERUM.get(), models().cross(blockTexture(CSBlocks.WILD_SOMNIFERUM.get()).getPath(),
                blockTexture(CSBlocks.WILD_SOMNIFERUM.get())).renderType("cutout"));
        makeCrop((CropBlock) CSBlocks.SOMNIFERUM_CLUSTER.get(), "somniferum_stage", "somniferum_stage", 4);

    }

    private void blockWithItem(Supplier<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));

    }

    private void pillarBlockHorizontal(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
        axisBlock(block,
                models().cubeColumn(name(block), side, end),
                models().cubeColumnHorizontal(name(block) + "_horizontal", side, end));
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);

    }

    private void CustomHorizontalBlock(Block block, String modelName) {
        ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/" + modelName));
        horizontalBlock(block, model);
    }

    private void CustomHorizontalLightBlock(Block block, String modelName) {
        ModelFile modelOn = new ModelFile.UncheckedModelFile(modLoc("block/" + modelName + "_on"));
        ModelFile modelOff = new ModelFile.UncheckedModelFile(modLoc("block/" + modelName));
        horizontalBlock(block, state -> state.getValue(BlockStateProperties.LIT) ? modelOn : modelOff);
    }

    private void CustomDirectionalBlock(Block block, String modelName) {
        ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/" + modelName));
        directionalBlock(block, model);
    }

    private void CustomBlock(Block block, String modelName) {
        ModelFile model = new ModelFile.UncheckedModelFile(modLoc("block/" + modelName));
        simpleBlock(block, model);
    }

    private void blockWithDifferentSides(Block block, String topTexture, String bottomTexture, String sideTexture, String particleTexture) {
        ModelFile model = models().cube(name(block),
                        modLoc("block/" + bottomTexture),
                        modLoc("block/" + topTexture),
                        modLoc("block/" + sideTexture),
                        modLoc("block/" + sideTexture),
                        modLoc("block/" + sideTexture),
                        modLoc("block/" + sideTexture))
                .texture("particle", modLoc("block/" + particleTexture));

        simpleBlock(block, model);
    }

    private void blockWithConfiguredSides(Block block, String topTexture, String bottomTexture, String frontTexture, String backTexture, String leftTexture, String rightTexture, String particleTexture) {
        ModelFile model = models().cube(name(block),
                        modLoc("block/" + bottomTexture),
                        modLoc("block/" + topTexture),
                        modLoc("block/" + frontTexture),
                        modLoc("block/" + backTexture),
                        modLoc("block/" + leftTexture),
                        modLoc("block/" + rightTexture))
                .texture("particle", modLoc("block/" + particleTexture));

        simpleBlock(block, model);
    }

    private void horizontalblockWithConfiguredSides(Block block, String topTexture, String bottomTexture, String frontTexture, String backTexture, String leftTexture, String rightTexture, String particleTexture) {
        ModelFile model = models().cube(name(block),
                        modLoc("block/" + bottomTexture),
                        modLoc("block/" + topTexture),
                        modLoc("block/" + frontTexture),
                        modLoc("block/" + backTexture),
                        modLoc("block/" + leftTexture),
                        modLoc("block/" + rightTexture))
                .texture("particle", modLoc("block/" + particleTexture));

        horizontalBlock(block, model);
    }

    public void makeCrop(CropBlock block, String modelName, String textureName, int stages) {
        Function<BlockState, ConfiguredModel[]> function = state -> cropStates(state, block, modelName, textureName, stages);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cropStates(BlockState state, CropBlock block, String modelName, String textureName, int stages) {
        int age = state.getValue(((CSCropBlock) block).getAgeProperty());
        int stage = (int) Math.floor((double) age / block.getMaxAge() * (stages - 1));

        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + stage,
                new ResourceLocation(CSAugmentations.MOD_ID, "block/" + textureName + stage)).renderType("cutout"));

        return models;
    }
}