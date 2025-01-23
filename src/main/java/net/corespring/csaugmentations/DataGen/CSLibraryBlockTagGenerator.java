package net.corespring.csaugmentations.DataGen;


import net.corespring.cslibrary.CSLibrary;
import net.corespring.cslibrary.Registry.CSTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CSLibraryBlockTagGenerator extends BlockTagsProvider {
    public CSLibraryBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CSLibrary.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(CSTags.Blocks.NEEDS_HAMMER_TOOL);


    }

}
