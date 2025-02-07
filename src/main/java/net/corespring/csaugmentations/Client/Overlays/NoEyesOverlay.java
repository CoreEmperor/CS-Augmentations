package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class NoEyesOverlay implements IGuiOverlay {
    private static final ResourceLocation NO_EYES = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/no_eyes.png");
    protected int screenWidth;
    protected int screenHeight;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics pGuiGraphics, float partialTicks, int screenWidth, int screenHeight) {
        this.screenWidth = pGuiGraphics.guiWidth();
        this.screenHeight = pGuiGraphics.guiHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            updateSightStatus(pGuiGraphics, player);
        }
    }

    public void updateSightStatus(GuiGraphics pGuiGraphics, Player player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(organData -> {
            if (!OrganCap.getOrganData(player).isPresent(CSAugUtil.OrganSlots.BRAIN) || !OrganCap.getOrganData(player).isPresent(CSAugUtil.OrganSlots.EYES) && !player.isSpectator()) {
                this.renderTextureOverlay(pGuiGraphics, NO_EYES, 1F);
            }
        });
    }

    protected void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
