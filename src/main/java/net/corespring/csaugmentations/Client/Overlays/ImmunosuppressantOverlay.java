package net.corespring.csaugmentations.Client.Overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ImmunosuppressantOverlay implements IGuiOverlay {
    private static final ResourceLocation IMMUNOSUPPRESSANT = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/immunosuppressant_overlay.png");
    private static final int FADE_DURATION = 20;
    public static int remainingDisplayTicks = 0;
    protected int screenWidth;
    protected int screenHeight;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics pGuiGraphics, float partialTicks, int screenWidth, int screenHeight) {
        this.screenWidth = pGuiGraphics.guiWidth();
        this.screenHeight = pGuiGraphics.guiHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null && remainingDisplayTicks > 0) {
            float alpha = 1.0F;
            if (remainingDisplayTicks < FADE_DURATION) {
                alpha = remainingDisplayTicks / (float) FADE_DURATION;
            }
            renderTextureOverlay(pGuiGraphics, IMMUNOSUPPRESSANT, alpha);
            remainingDisplayTicks--;
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
