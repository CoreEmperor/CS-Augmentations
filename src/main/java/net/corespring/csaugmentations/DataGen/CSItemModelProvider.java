package net.corespring.csaugmentations.DataGen;


import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CSItemModelProvider extends ItemModelProvider {

    public CSItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CSAugmentations.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_BRAIN);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_EYES);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_HEART);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_KIDNEY);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_LIVER);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_LUNGS);
        simpleItem((RegistryObject<Item>) CSItems.NATURAL_STOMACH);

        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_EYES);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_HEART);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_KIDNEY);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_LIVER);
        simpleItem((RegistryObject<Item>) CSItems.PROSTHETIC_STOMACH);

        evenSimplerBlockItem(CSBlocks.CULTIVATOR);
        evenSimplerBlockItem(CSBlocks.REFINERY);
        evenSimplerBlockItem(CSBlocks.CHEMISTRY_TABLE);

        simpleItem((RegistryObject<Item>) CSItems.UNFINISHED_MORTAR);
        simpleItem((RegistryObject<Item>) CSItems.UNFINISHED_PESTLE);
        simpleItem((RegistryObject<Item>) CSItems.MORTAR);
        simpleItem((RegistryObject<Item>) CSItems.PESTLE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_PETRI_DISH);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_PETRI_DISH);

        handheldItem((RegistryObject<Item>) CSItems.SCALPEL);
        simpleItem((RegistryObject<Item>) CSItems.HEMOSTAT);
        simpleItem((RegistryObject<Item>) CSItems.RETRACTORS);
        simpleItem((RegistryObject<Item>) CSItems.SUTURE);

        simpleItem((RegistryObject<Item>) CSItems.LOADED_CRUDE_PETRI_DISH);
        simpleItem((RegistryObject<Item>) CSItems.LOADED_REFINED_PETRI_DISH);

        simpleItem((RegistryObject<Item>) CSItems.CRUSHED_ACID_MUSHROOM);

        simpleItem((RegistryObject<Item>) CSItems.HYDRAULIC);
        simpleItem((RegistryObject<Item>) CSItems.STEEL_WIRE);
        simpleItem((RegistryObject<Item>) CSItems.FUSE);
        simpleItem((RegistryObject<Item>) CSItems.TRANSFORMER);
        simpleItem((RegistryObject<Item>) CSItems.POWER_CONTROL_MODULE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_LOGIC_COMPONENT);

        simpleItem((RegistryObject<Item>) CSItems.DMEM);
        simpleItem((RegistryObject<Item>) CSItems.NUTRIOGLOOP);
        simpleItem((RegistryObject<Item>) CSItems.NBS);

        handheldItem((RegistryObject<Item>) CSItems.REINFORCED_BONE);
        simpleItem((RegistryObject<Item>) CSItems.BALL_SOCKET_JOINT);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_BLOOD_PUMP);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_BLOOD_FILTER);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_OPTICAL_SENSOR);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_MUSCLE);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_TISSUE);
        simpleItem((RegistryObject<Item>) CSItems.ARTIFICIAL_NERVES);
        simpleItem((RegistryObject<Item>) CSItems.BRASS_LIMB_BASE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_AUGMENTED_MUSCLE);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_AUGMENTED_TISSUE);

        simpleItem((RegistryObject<Item>) CSItems.FOSSIL);

        simpleItem((RegistryObject<Item>) CSItems.CRUDE_OIL);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_OIL);
        simpleItem((RegistryObject<Item>) CSItems.CRUDE_PLASTIC);
        simpleItem((RegistryObject<Item>) CSItems.REFINED_PLASTIC);

        simpleItem((RegistryObject<Item>) CSItems.DUST_SALT);

        simpleItem((RegistryObject<Item>) CSItems.MEAD);

        simpleItem((RegistryObject<Item>) CSItems.BEAKER_SILK);
        simpleItem((RegistryObject<Item>) CSItems.SOMNIFERUM_SAP);
        simpleItem((RegistryObject<Item>) CSItems.SOMNIFERUM_SEEDPOD);

        evenSimplerBlockItem(CSBlocks.SALT_SLAB);
        evenSimplerBlockItem(CSBlocks.SALT_STAIRS);
        evenSimplerBlockItem(CSBlocks.POLISHED_SALT_SLAB);
        evenSimplerBlockItem(CSBlocks.POLISHED_SALT_STAIRS);

        simpleBlockItemBlockTexture((RegistryObject<Block>) CSBlocks.WILD_SOMNIFERUM);

    }

    public void trapdoorItem(Supplier<Block> block) {
        this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath() + "_bottom"));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder complexBlockItem(RegistryObject<Block> item, String texturename) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + texturename));
    }

    public void evenSimplerBlockItem(Supplier<Block> block) {
        this.withExistingParent(CSAugmentations.MOD_ID + ":" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath(),
                modLoc("block/" + BuiltInRegistries.BLOCK.getKey(block.get()).getPath()));
    }


    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "item/" + item.getId().getPath()));

    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CSAugmentations.MOD_ID, "block/" + item.getId().getPath()));
    }
}