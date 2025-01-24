package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.Augmentations.Limbs.Cybernetic.CyberArm;
import net.corespring.csaugmentations.Augmentations.Limbs.Cybernetic.CyberLeg;
import net.corespring.csaugmentations.Augmentations.Limbs.Natural.NaturalArm;
import net.corespring.csaugmentations.Augmentations.Limbs.Natural.NaturalLeg;
import net.corespring.csaugmentations.Augmentations.Limbs.Prosthetic.ProstheticArm;
import net.corespring.csaugmentations.Augmentations.Limbs.Prosthetic.ProstheticLeg;
import net.corespring.csaugmentations.Augmentations.Organs.Natural.*;
import net.corespring.csaugmentations.Augmentations.Organs.Prosthetic.*;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Item.PetriDish;
import net.corespring.csaugmentations.Item.Pharma.MeadItem;
import net.corespring.csaugmentations.Item.Pharma.SilkItem;
import net.corespring.csaugmentations.Item.SurgicalEquipment.Hemostat;
import net.corespring.csaugmentations.Item.SurgicalEquipment.Scalpel;
import net.corespring.csaugmentations.Item.SurgicalEquipment.SurgicalEquipmentItem;
import net.corespring.csaugmentations.Item.SurgicalEquipment.Suture;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.cslibrary.Items.BeakerItems.BasicBeakerItem;
import net.corespring.cslibrary.Items.CRDItem;
import net.corespring.cslibrary.Items.CRItem;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CSItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CSAugmentations.MOD_ID);

    public static final Supplier<Item> NATURAL_LEG = ITEMS.register("natural_leg", () -> new NaturalLeg(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_ARM = ITEMS.register("natural_arm", () -> new NaturalArm(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_BRAIN = ITEMS.register("natural_brain", () -> new NaturalBrain(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_EYES = ITEMS.register("natural_eyes", () -> new NaturalEyes(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_HEART = ITEMS.register("natural_heart", () -> new NaturalHeart(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_KIDNEY = ITEMS.register("natural_kidney", () -> new NaturalKidney(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_LIVER = ITEMS.register("natural_liver", () -> new NaturalLiver(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_LUNGS = ITEMS.register("natural_lungs", () -> new NaturalLungs(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_RIBS = ITEMS.register("natural_ribs", () -> new NaturalRibs(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_SPINE = ITEMS.register("natural_spine", () -> new NaturalSpine(CSOrganTiers.NATURAL, pOrganItem()));
    public static final Supplier<Item> NATURAL_STOMACH = ITEMS.register("natural_stomach", () -> new NaturalStomach(CSOrganTiers.NATURAL, pOrganItem()));

    public static final Supplier<Item> PROSTHETIC_LEG = ITEMS.register("prosthetic_leg", () -> new ProstheticLeg(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_ARM = ITEMS.register("prosthetic_arm", () -> new ProstheticArm(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_HEART = ITEMS.register("prosthetic_heart", () -> new ProstheticHeart(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_KIDNEY = ITEMS.register("prosthetic_kidney", () -> new ProstheticKidney(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_LIVER = ITEMS.register("prosthetic_liver", () -> new ProstheticLiver(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_LUNGS = ITEMS.register("prosthetic_lungs", () -> new ProstheticLungs(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_RIBS = ITEMS.register("prosthetic_ribs", () -> new ProstheticRibs(CSOrganTiers.PROSTHETIC, pOrganItem()));
    public static final Supplier<Item> PROSTHETIC_STOMACH = ITEMS.register("prosthetic_stomach", () -> new ProstheticStomach(CSOrganTiers.PROSTHETIC, pOrganItem()));

    //public static final Supplier<Item> CYBER_LEG = ITEMS.register("cyber_leg", () -> new CyberLeg(CSOrganTiers.CYBERNETIC, pOrganItem()));
    //public static final Supplier<Item> CYBER_ARM = ITEMS.register("cyber_arm", () -> new CyberArm(CSOrganTiers.CYBERNETIC, pOrganItem()));

    public static final Supplier<Item> UNFINISHED_MORTAR = ITEMS.register("unfinished_mortar",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> UNFINISHED_PESTLE = ITEMS.register("unfinished_pestle",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> MORTAR = ITEMS.register("mortar",
            () -> new CRItem(new Item.Properties()));
    public static final Supplier<Item> PESTLE = ITEMS.register("pestle",
            () -> new CRDItem(new Item.Properties().durability(20)));
    public static final Supplier<Item> CRUDE_PETRI_DISH = ITEMS.register("crude_petri_dish",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> REFINED_PETRI_DISH = ITEMS.register("refined_petri_dish",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> SCALPEL = ITEMS.register("scalpel",
            () -> new Scalpel(new Item.Properties().stacksTo(1).durability(10)));
    public static final Supplier<Item> HEMOSTAT = ITEMS.register("hemostat",
            () -> new Hemostat(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> RETRACTORS = ITEMS.register("retractors",
            () -> new SurgicalEquipmentItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> SUTURE = ITEMS.register("suture",
            () -> new Suture(new Item.Properties().stacksTo(16)));

    public static final Supplier<Item> LOADED_CRUDE_PETRI_DISH = ITEMS.register("loaded_crude_petri_dish",
            () -> new PetriDish(new Item.Properties()));
    public static final Supplier<Item> LOADED_REFINED_PETRI_DISH = ITEMS.register("loaded_refined_petri_dish",
            () -> new PetriDish(new Item.Properties()));

    //Crafting Components//==================================================================//

    public static final Supplier<Item> CRUSHED_ACID_MUSHROOM = ITEMS.register("crushed_acid_mushroom", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> HYDRAULIC = ITEMS.register("hydraulic",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> STEEL_WIRE = ITEMS.register("steel_wire",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> FUSE = ITEMS.register("fuse",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> TRANSFORMER = ITEMS.register("transformer",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> POWER_CONTROL_MODULE = ITEMS.register("power_control_module",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_LOGIC_COMPONENT = ITEMS.register("crude_logic_component",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> DMEM = ITEMS.register("dmem",
            () -> new BasicBeakerItem(new Item.Properties()));
    public static final Supplier<Item> NUTRIOGLOOP = ITEMS.register("nutriogloop",
            () -> new Item(new Item.Properties()));
    public static final Supplier<Item> NBS = ITEMS.register("nbs",
            () -> new Item(new Item.Properties()));

    public static final Supplier<Item> REINFORCED_BONE = ITEMS.register("reinforced_bone", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BALL_SOCKET_JOINT = ITEMS.register("ball_socket_joint", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_BLOOD_PUMP = ITEMS.register("crude_blood_pump", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_BLOOD_FILTER = ITEMS.register("crude_blood_filter", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ARTIFICIAL_MUSCLE = ITEMS.register("artificial_muscle", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ARTIFICIAL_TISSUE = ITEMS.register("artificial_tissue", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> ARTIFICIAL_NERVES = ITEMS.register("artificial_nerves", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BRASS_LIMB_BASE = ITEMS.register("brass_limb_base", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_AUGMENTED_MUSCLE = ITEMS.register("crude_augmented_muscle", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_AUGMENTED_TISSUE = ITEMS.register("crude_augmented_tissue", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> FOSSIL = ITEMS.register("fossil", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> CRUDE_OIL = ITEMS.register("crude_oil", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> REFINED_OIL = ITEMS.register("refined_oil", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> CRUDE_PLASTIC = ITEMS.register("crude_plastic", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> REFINED_PLASTIC = ITEMS.register("refined_plastic", () -> new Item(new Item.Properties()));

    public static final Supplier<Item> DUST_SALT = ITEMS.register("dust_salt", () -> new Item(new Item.Properties()));

    //Alcohol Crafting Components//
    public static final Supplier<Item> MEAD = ITEMS.register("mead", () -> new MeadItem(1, new Item.Properties().food(CSEdibleFoods.MEAD).stacksTo(16)));

    //Pharmaceutical Crafting Components//
    public static final Supplier<Item> SOMNIFERUM_SEEDPOD = ITEMS.register("somniferum_seedpod", () -> new ItemNameBlockItem(CSBlocks.SOMNIFERUM_CLUSTER.get(), new Item.Properties()));
    public static final Supplier<Item> SOMNIFERUM_SAP = ITEMS.register("somniferum_sap", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> BEAKER_SILK = ITEMS.register("beaker_silk", () -> new SilkItem(new Item.Properties().stacksTo(24)));

    //=======================================================================================================

    public static Item.Properties pOrganItem() {
        return new Item.Properties().stacksTo(1);
    }


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
