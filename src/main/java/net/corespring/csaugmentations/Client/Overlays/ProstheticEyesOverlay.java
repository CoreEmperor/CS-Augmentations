package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ProstheticEyesOverlay implements IGuiOverlay {
    private static final ResourceLocation PROSTHETIC_EYES = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/prosthetic_eyes_overlay.png");
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
            if (OrganCap.getOrganData(player).getStackInSlot(CSAugUtil.OrganSlots.EYES).is(CSItems.PROSTHETIC_EYES.get()) && !player.isSpectator()) {
                this.renderTextureOverlay(pGuiGraphics, PROSTHETIC_EYES, 1F);
            }
        });
    }

    protected void renderTextureOverlay(GuiGraphics pGuiGraphics, ResourceLocation pShaderLocation, float pAlpha) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, pAlpha);
        pGuiGraphics.blit(pShaderLocation, 0, 0, -90, 0.0F, 0.0F, this.screenWidth, this.screenHeight, this.screenWidth, this.screenHeight);
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
