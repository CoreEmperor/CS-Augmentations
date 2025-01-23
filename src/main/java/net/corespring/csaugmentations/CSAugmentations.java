package net.corespring.csaugmentations;

import com.mojang.logging.LogUtils;
import net.corespring.csaugmentations.Client.Overlays.NoEyesOverlay;
import net.corespring.csaugmentations.Client.Overlays.SilkBlissOverlay;
import net.corespring.csaugmentations.Utility.Network.CSNetwork;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.*;
import net.corespring.csaugmentations.Utility.Events.ForgeEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CSAugmentations.MOD_ID)
public class CSAugmentations {
    public static final String MOD_ID = "csaugmentations";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CSAugmentations() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        CSItems.register(modEventBus);
        CSBlocks.register(modEventBus);
        CSBlockEntities.register(modEventBus);
        CSEffects.register(modEventBus);
        CSMenu.register(modEventBus);
        CSRecipeSerializers.register(modEventBus);
        CSRecipeTypes.register(modEventBus);
        CSCreativeTab.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(ForgeEvents.InventoryEvents.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CSCommonConfigs.SPEC, "csaugmentations-common.toml");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CSNetwork.init();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(NoEyesOverlay.class);
        MinecraftForge.EVENT_BUS.register(SilkBlissOverlay.class);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EventSubscriber {

        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("no_eyes_overlay", new NoEyesOverlay());
            event.registerBelowAll("silk_bliss_overlay", new SilkBlissOverlay());
        }
    }
}
