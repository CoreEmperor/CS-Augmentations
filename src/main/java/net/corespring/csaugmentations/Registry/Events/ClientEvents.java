package net.corespring.csaugmentations.Registry.Events;

import com.mojang.blaze3d.shaders.AbstractUniform;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Client.Overlays.*;
import net.corespring.csaugmentations.Registry.Network.CSNetwork;
import net.corespring.csaugmentations.Registry.Network.Packets.C2SToggleArmBuffsPacket;
import net.corespring.csaugmentations.Registry.Network.Packets.C2SToggleLegBuffsPacket;
import net.corespring.csaugmentations.Registry.Network.Packets.S2CSyncDataPacket;
import net.corespring.csaugmentations.Registry.Utility.CSAugUtil;
import net.corespring.csaugmentations.Registry.Utility.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (KeyBinding.ZOOM_KEY.consumeClick()) {
                // Handle zoom key
            }
            if (KeyBinding.NVG_KEY.consumeClick()) {
                // Handle NVG key
            }
            if (KeyBinding.ARMS_KEY.consumeClick()) {
                CSAugUtil.armsEnabled = !CSAugUtil.armsEnabled;
                CSNetwork.NETWORK_CHANNEL.sendToServer(new C2SToggleArmBuffsPacket(CSAugUtil.armsEnabled));
            }
            if (KeyBinding.LEGS_KEY.consumeClick()) {
                CSAugUtil.legsEnabled = !CSAugUtil.legsEnabled;
                CSNetwork.NETWORK_CHANNEL.sendToServer(new C2SToggleLegBuffsPacket(CSAugUtil.legsEnabled));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.ZOOM_KEY);
            event.register(KeyBinding.NVG_KEY);
            event.register(KeyBinding.ARMS_KEY);
            event.register(KeyBinding.LEGS_KEY);
        }

        public static void updateClientCapability(S2CSyncDataPacket packet) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getId() == packet.getPlayerId()) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
                    data.deserializeNBT(packet.getOrganData().serializeNBT());
                    if (player.containerMenu instanceof net.corespring.csaugmentations.Client.Menus.AugmentMenu menu) {
                        menu.broadcastChanges();
                    }
                });
            }
        }

        @SubscribeEvent
        public static void registerOverlays(RegisterGuiOverlaysEvent event) {
            event.registerBelowAll("no_eyes_overlay", new NoEyesOverlay());
            event.registerBelowAll("prosthetic_eyes_overlay", new ProstheticEyesOverlay());
            event.registerBelowAll("silk_bliss_overlay", new SilkBlissOverlay());
            event.registerBelowAll("intoxicated_overlay", new IntoxicatedOverlay());
            event.registerBelowAll("liver_failure_overlay", new LiverFailureOverlay());
            event.registerBelowAll("organ_rejection_overlay", new OrganRejectionOverlay());
            event.registerBelowAll("immunosuppressant_overlay", new ImmunosuppressantOverlay());
            event.registerBelowAll("test_overlay", new TestOverlay());
        }
    }
}