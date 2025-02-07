package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CSAugmentations.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CS_AUGMENTATIONS_TAB = CREATIVE_MODE_TABS.register("cs_augmentations_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CSItems.PROSTHETIC_ARM.get()))
                    .title(Component.translatable("itemGroup.cs_augmentations_tab"))
                    .displayItems((pParameters, pOutput) -> {

                        pOutput.accept(CSBlocks.CULTIVATOR.get());
                        pOutput.accept(CSBlocks.REFINERY.get());
                        pOutput.accept(CSBlocks.CHEMISTRY_TABLE.get());
                        pOutput.accept(CSBlocks.FABRICATOR.get());

                        pOutput.accept(CSBlocks.FOSSIL_ORE.get());
                        pOutput.accept(CSBlocks.DEEPSLATE_FOSSIL_ORE.get());
                        pOutput.accept(CSBlocks.FOSSIL_BLOCK.get());

                        pOutput.accept(CSBlocks.SALT.get());
                        pOutput.accept(CSBlocks.SALT_SLAB.get());
                        pOutput.accept(CSBlocks.SALT_STAIRS.get());
                        pOutput.accept(CSBlocks.POLISHED_SALT.get());
                        pOutput.accept(CSBlocks.POLISHED_SALT_SLAB.get());
                        pOutput.accept(CSBlocks.POLISHED_SALT_STAIRS.get());

                        pOutput.accept(CSItems.NATURAL_LEG.get());
                        pOutput.accept(CSItems.NATURAL_ARM.get());
                        pOutput.accept(CSItems.NATURAL_BRAIN.get());
                        pOutput.accept(CSItems.NATURAL_EYES.get());
                        pOutput.accept(CSItems.NATURAL_HEART.get());
                        pOutput.accept(CSItems.NATURAL_KIDNEY.get());
                        pOutput.accept(CSItems.NATURAL_LIVER.get());
                        pOutput.accept(CSItems.NATURAL_LUNGS.get());
                        pOutput.accept(CSItems.NATURAL_RIBS.get());
                        pOutput.accept(CSItems.NATURAL_SPINE.get());
                        pOutput.accept(CSItems.NATURAL_STOMACH.get());

                        pOutput.accept(CSItems.PROSTHETIC_LEG.get());
                        pOutput.accept(CSItems.PROSTHETIC_ARM.get());
                        pOutput.accept(CSItems.PROSTHETIC_EYES.get());
                        pOutput.accept(CSItems.PROSTHETIC_HEART.get());
                        pOutput.accept(CSItems.PROSTHETIC_KIDNEY.get());
                        pOutput.accept(CSItems.PROSTHETIC_LIVER.get());
                        pOutput.accept(CSItems.PROSTHETIC_LUNGS.get());
                        pOutput.accept(CSItems.PROSTHETIC_RIBS.get());
                        pOutput.accept(CSItems.PROSTHETIC_STOMACH.get());

                        pOutput.accept(CSItems.CYBER_LEG.get());
                        pOutput.accept(CSItems.CYBER_ARM.get());
                        pOutput.accept(CSItems.CYBER_BRAIN.get());
                        pOutput.accept(CSItems.CYBER_BRAIN_IMPLANT.get());
                        pOutput.accept(CSItems.CYBER_RIBS.get());
                        pOutput.accept(CSItems.CYBER_SPINE.get());
                        pOutput.accept(CSItems.CYBER_WARPER.get());

                        pOutput.accept(CSItems.UNFINISHED_MORTAR.get());
                        pOutput.accept(CSItems.UNFINISHED_PESTLE.get());
                        pOutput.accept(CSItems.MORTAR.get());
                        pOutput.accept(CSItems.PESTLE.get());
                        pOutput.accept(CSItems.CRUDE_PETRI_DISH.get());
                        pOutput.accept(CSItems.REFINED_PETRI_DISH.get());

                        pOutput.accept(CSItems.NEURAL_ANALYZER.get());
                        pOutput.accept(CSItems.SCALPEL.get());
                        pOutput.accept(CSItems.HEMOSTAT.get());
                        pOutput.accept(CSItems.RETRACTORS.get());
                        pOutput.accept(CSItems.SUTURE.get());

                        pOutput.accept(CSItems.LOADED_CRUDE_PETRI_DISH.get());
                        pOutput.accept(CSItems.LOADED_REFINED_PETRI_DISH.get());

                        pOutput.accept(CSItems.BLUEPRINT.get());

                        pOutput.accept(CSItems.CRUSHED_ACID_MUSHROOM.get());

                        pOutput.accept(CSItems.NEURAL_INTERFACE.get());
                        pOutput.accept(CSItems.HYDRAULIC.get());
                        pOutput.accept(CSItems.STEEL_WIRE.get());
                        pOutput.accept(CSItems.FUSE.get());
                        pOutput.accept(CSItems.TRANSFORMER.get());
                        pOutput.accept(CSItems.POWER_CONTROL_MODULE.get());
                        pOutput.accept(CSItems.CRUDE_LOGIC_COMPONENT.get());
                        pOutput.accept(CSItems.REFINED_LOGIC_COMPONENT.get());

                        pOutput.accept(CSItems.DMEM.get());
                        pOutput.accept(CSItems.NUTRIOGLOOP.get());
                        pOutput.accept(CSItems.NBS.get());

                        pOutput.accept(CSItems.REINFORCED_BONE.get());
                        pOutput.accept(CSItems.BALL_SOCKET_JOINT.get());
                        pOutput.accept(CSItems.CRUDE_BLOOD_PUMP.get());
                        pOutput.accept(CSItems.CRUDE_BLOOD_FILTER.get());
                        pOutput.accept(CSItems.CRUDE_OPTICAL_SENSOR.get());
                        pOutput.accept(CSItems.ARTIFICIAL_MUSCLE.get());
                        pOutput.accept(CSItems.ARTIFICIAL_TISSUE.get());
                        pOutput.accept(CSItems.ARTIFICIAL_NERVES.get());
                        pOutput.accept(CSItems.BRASS_LIMB_BASE.get());
                        pOutput.accept(CSItems.CRUDE_AUGMENTED_MUSCLE.get());
                        pOutput.accept(CSItems.CRUDE_AUGMENTED_TISSUE.get());
                        pOutput.accept(CSItems.REFINED_AUGMENTED_MUSCLE.get());
                        pOutput.accept(CSItems.REFINED_AUGMENTED_TISSUE.get());

                        pOutput.accept(CSItems.FOSSIL.get());

                        pOutput.accept(CSItems.CRUDE_OIL.get());
                        pOutput.accept(CSItems.REFINED_OIL.get());
                        pOutput.accept(CSItems.CRUDE_PLASTIC.get());
                        pOutput.accept(CSItems.REFINED_PLASTIC.get());

                        pOutput.accept(CSItems.DUST_SALT.get());

                        pOutput.accept(CSItems.MEAD.get());

                        pOutput.accept(CSBlocks.CYCLOFUNGI.get());
                        pOutput.accept(CSItems.BEAKER_CYCLOSPORINE.get());
                        pOutput.accept(CSItems.BEAKER_SPORINE.get());
                        pOutput.accept(CSItems.IMMUNOSUPPRESSANT.get());
                        pOutput.accept(CSItems.REFINED_IMMUNOSUPPRESSANT.get());

                        pOutput.accept(CSBlocks.BLOCK_SOMNIFERUM_SAP.get());
                        pOutput.accept(CSBlocks.WILD_SOMNIFERUM.get());
                        pOutput.accept(CSItems.SOMNIFERUM_SEEDPOD.get());
                        pOutput.accept(CSItems.SOMNIFERUM_SAP.get());
                        pOutput.accept(CSItems.BEAKER_SILK.get());

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
