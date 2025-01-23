package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.Block.BlockEntities.ChemistryBlockEntity;
import net.corespring.csaugmentations.Block.BlockEntities.RefineryBlockEntity;
import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CSAugmentations.MOD_ID);

    public static final Supplier<BlockEntityType<RefineryBlockEntity>> REFINERY_BE =
            BLOCK_ENTITY.register("refinery_be", () ->
                    BlockEntityType.Builder.of(RefineryBlockEntity::new,
                            CSBlocks.REFINERY.get()).build(null));

    public static final Supplier<BlockEntityType<ChemistryBlockEntity>> CHEMISTRY_TABLE_BE =
            BLOCK_ENTITY.register("chemistry_table_be", () ->
                    BlockEntityType.Builder.of(ChemistryBlockEntity::new,
                            CSBlocks.CHEMISTRY_TABLE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }
}
