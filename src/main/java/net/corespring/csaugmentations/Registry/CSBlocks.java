package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.Block.*;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.cslibrary.Blocks.Plants.CSOceanShroom;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class CSBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CSAugmentations.MOD_ID);

    public static final Supplier<Block> INVISIBLE_CHEM = registerBlock("invisible_chem",
            () -> new InvisibleChemBlock(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.COPPER).noOcclusion().noLootTable()));
    public static final Supplier<Block> INVISIBLE_FAB = registerBlock("invisible_fab",
            () -> new InvisibleFabBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().noLootTable().lightLevel(state -> 8)));
    public static final Supplier<Block> CULTIVATOR = registerBlock("cultivator",
            () -> new CultivatorBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)));
    public static final Supplier<Block> REFINERY = registerBlock("refinery",
            () -> new RefineryBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL).lightLevel(litBlockEmission(15))));
    public static final Supplier<Block> CHEMISTRY_TABLE = registerBlock("chemistry_table",
            () -> new ChemistryBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().noLootTable(), INVISIBLE_CHEM.get()));
    public static final Supplier<Block> FABRICATOR = registerBlock("fabricator",
            () -> new FabricatorBlock(BlockBehaviour.Properties.of().strength(4f).sound(SoundType.COPPER).noOcclusion().lightLevel(state -> 8), INVISIBLE_FAB.get()));

    public static final Supplier<Block> FOSSIL_ORE = registerBlock("fossil_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.STONE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> DEEPSLATE_FOSSIL_ORE = registerBlock("deepslate_fossil_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(6f).sound(SoundType.DEEPSLATE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> FOSSIL_BLOCK = registerBlock("fossil_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f).sound(SoundType.BONE_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final Supplier<Block> SALT = registerBlock("salt",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> SALT_SLAB = registerBlock("salt_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> SALT_STAIRS = registerBlock("salt_stairs",
            () -> new StairBlock(SALT.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_SALT = registerBlock("polished_salt",
            () -> new Block(BlockBehaviour.Properties.of().strength(3f).sound(SoundType.CALCITE)
                    .requiresCorrectToolForDrops()));
    public static final Supplier<Block> POLISHED_SALT_SLAB = registerBlock("polished_salt_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));
    public static final Supplier<Block> POLISHED_SALT_STAIRS = registerBlock("polished_salt_stairs",
            () -> new StairBlock(POLISHED_SALT.get().defaultBlockState(), BlockBehaviour.Properties.of()
                    .strength(2F, 6f).requiresCorrectToolForDrops().sound(SoundType.CALCITE)));

    public static final Supplier<Block> CYCLOFUNGI = registerBlock("cyclofungi",
            () -> new CSMushroomBlock(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM).noOcclusion().noCollission().lightLevel((state) -> 1)));

    public static final Supplier<Block> WILD_SOMNIFERUM = registerBlock("wild_somniferum",
            () -> new FlowerBlock(MobEffects.CONFUSION,5, BlockBehaviour.Properties.copy(Blocks.POPPY).noOcclusion().noCollission()));
    public static final Supplier<Block> SOMNIFERUM_CLUSTER = registerBlock("somniferum_cluster",
            () -> new SomniferumCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final Supplier<Block> BLOCK_SOMNIFERUM_SAP = registerBlock("block_somniferum_sap",
            () -> new HoneyBlock(BlockBehaviour.Properties.of().strength(1f).sound(SoundType.HONEY_BLOCK)));


    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }

    private static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return CSItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
