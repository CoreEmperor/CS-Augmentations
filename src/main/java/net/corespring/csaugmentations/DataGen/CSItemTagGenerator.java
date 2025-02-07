package net.corespring.csaugmentations.DataGen;


import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Registry.CSTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CSItemTagGenerator extends ItemTagsProvider {
    public CSItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                              CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CSAugmentations.MOD_ID, existingFileHelper);
    }


    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(CSTags.Items.MORTARS)
                .add(CSItems.MORTAR.get(),
                        CSItems.UNFINISHED_MORTAR.get());

        this.tag(CSTags.Items.PESTLES)
                .add(CSItems.PESTLE.get(),
                        CSItems.UNFINISHED_PESTLE.get());

        this.tag(CSTags.Items.BLOOD_FILTERS)
                .add(CSItems.CRUDE_BLOOD_FILTER.get());

        this.tag(CSTags.Items.RAW_MEATS)
                .add(Items.BEEF,
                        Items.PORKCHOP,
                        Items.MUTTON,
                        Items.CHICKEN,
                        Items.RABBIT,
                        Items.COD,
                        Items.TROPICAL_FISH,
                        Items.SALMON,
                        net.corespring.cslibrary.Registry.CSItems.FLESH.get());

    }
}

