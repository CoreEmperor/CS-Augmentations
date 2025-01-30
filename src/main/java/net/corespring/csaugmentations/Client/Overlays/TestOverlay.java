package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class TestOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null) {
            player.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                int severityLevel = cap.getCyberpsychosis().getSeverityLevel();
                String text = "Cyberpsychosis Severity: " + severityLevel;
                guiGraphics.drawString(minecraft.font, text, 10, 10, 0xFFFFFF);
            });
        }
    }
}