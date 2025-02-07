package net.corespring.csaugmentations.DataGen;


import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CSBlockTagGenerator extends BlockTagsProvider {
    public CSBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CSAugmentations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.CROPS)
                .add(CSBlocks.SOMNIFERUM_CLUSTER.get());

        this.tag(BlockTags.BEE_GROWABLES)
                .add(CSBlocks.SOMNIFERUM_CLUSTER.get());

        this.tag(BlockTags.FLOWERS)
                .add(CSBlocks.WILD_SOMNIFERUM.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CSBlocks.CULTIVATOR.get(),
                        CSBlocks.REFINERY.get(),
                        CSBlocks.FOSSIL_ORE.get(),
                        CSBlocks.DEEPSLATE_FOSSIL_ORE.get(),
                        CSBlocks.FOSSIL_BLOCK.get(),
                        CSBlocks.CHEMISTRY_TABLE.get(),
                        CSBlocks.INVISIBLE_CHEM.get(),
                        CSBlocks.FABRICATOR.get(),
                        CSBlocks.INVISIBLE_FAB.get(),
                        CSBlocks.SALT.get(),
                        CSBlocks.SALT_SLAB.get(),
                        CSBlocks.SALT_STAIRS.get(),
                        CSBlocks.POLISHED_SALT.get(),
                        CSBlocks.POLISHED_SALT_SLAB.get(),
                        CSBlocks.POLISHED_SALT_STAIRS.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE);

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL);

        this.tag(BlockTags.MINEABLE_WITH_HOE);

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(CSBlocks.SALT.get(),
                CSBlocks.SALT_SLAB.get(),
                CSBlocks.SALT_STAIRS.get(),
                CSBlocks.POLISHED_SALT.get(),
                CSBlocks.POLISHED_SALT_SLAB.get(),
                CSBlocks.POLISHED_SALT_STAIRS.get());


        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(CSBlocks.CULTIVATOR.get(),
                        CSBlocks.REFINERY.get(),
                        CSBlocks.FOSSIL_ORE.get(),
                        CSBlocks.DEEPSLATE_FOSSIL_ORE.get(),
                        CSBlocks.FOSSIL_BLOCK.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL);

        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL);

        this.tag(BlockTags.DOORS);

        this.tag(BlockTags.SOUL_FIRE_BASE_BLOCKS)
                .add(CSBlocks.BLOCK_SOMNIFERUM_SAP.get());


    }

}
