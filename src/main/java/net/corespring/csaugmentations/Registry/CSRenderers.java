package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Screens.AugmentScreen;
import net.corespring.csaugmentations.Client.Screens.ChemistryScreen;
import net.corespring.csaugmentations.Client.Screens.CultivatorScreen;
import net.corespring.csaugmentations.Client.Screens.RefineryScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CSRenderers {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(CSMenu.CULTIVATOR_MENU.get(), CultivatorScreen::new);
        MenuScreens.register(CSMenu.REFINERY_MENU.get(), RefineryScreen::new);
        MenuScreens.register(CSMenu.AUGMENT_MENU.get(), AugmentScreen::new);
        MenuScreens.register(CSMenu.CHEMISTRY_MENU.get(), ChemistryScreen::new);
    }

}
