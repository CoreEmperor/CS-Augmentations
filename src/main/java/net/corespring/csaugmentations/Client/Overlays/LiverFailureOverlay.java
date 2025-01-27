package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class LiverFailureOverlay implements IGuiOverlay {
    protected int screenWidth;
    protected int screenHeight;
    private static final ResourceLocation LIVER_FAILURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/liver_failure_overlay.png");

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics pGuiGraphics, float partialTicks, int screenWidth, int screenHeight) {
        this.screenWidth = pGuiGraphics.guiWidth();
        this.screenHeight = pGuiGraphics.guiHeight();
        Player player = Minecraft.getInstance().player;

        if(player != null && player.hasEffect(CSEffects.LIVER_FAILURE.get())) {
            renderTextureOverlay(pGuiGraphics, LIVER_FAILURE, 0.75F);
        }

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

