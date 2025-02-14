package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSMenu {
    public static final DeferredRegister<MenuType<?>> REGISTER =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, CSAugmentations.MOD_ID);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return REGISTER.register(name, () -> IForgeMenuType.create(factory));
    }

    public static final RegistryObject<MenuType<CultivatorMenu>> CULTIVATOR_MENU =
            registerMenuType("cultivator_menu", CultivatorMenu::new);
    public static final RegistryObject<MenuType<RefineryMenu>> REFINERY_MENU =
            registerMenuType("refinery_menu", RefineryMenu::new);
    public static final RegistryObject<MenuType<AugmentMenu>> AUGMENT_MENU =
            registerMenuType("augment_menu", AugmentMenu::new);
    public static final RegistryObject<MenuType<ChemistryMenu>> CHEMISTRY_MENU =
            registerMenuType("chemistry_menu", ChemistryMenu::new);
    public static final RegistryObject<MenuType<FabricatorMenu>> FABRICATOR_MENU =
            registerMenuType("fabricator_menu", FabricatorMenu::new);
    public static final RegistryObject<MenuType<SyringeGunMenu>> SYRINGE_MENU =
            registerMenuType("syringe_menu", SyringeGunMenu::new);

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
