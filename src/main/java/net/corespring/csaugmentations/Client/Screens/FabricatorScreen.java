package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.FabricatorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FabricatorScreen extends AbstractContainerScreen<FabricatorMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/fabricator_gui.png");

    public FabricatorScreen(FabricatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 208;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics gui, float partialTick, int mouseX, int mouseY) {
        gui.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int progress = menu.getProgress();
        int maxProgress = menu.getMaxProgress();

        int progressHeight = (int) (22 * (progress / (float) maxProgress));

        gui.blit(BG_LOCATION, leftPos + 102, topPos + 27 + (22 - progressHeight), 0, 166 + (22 - progressHeight), 37, progressHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        this.renderBg(pGuiGraphics, partialTicks, mouseX, mouseY);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        pGuiGraphics.drawString(this.font, this.title.getString(), 19, 3, 0x404040, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle.getString(), 45, this.imageHeight - 96 + 2, 0xFFFFFF, true);
    }
}