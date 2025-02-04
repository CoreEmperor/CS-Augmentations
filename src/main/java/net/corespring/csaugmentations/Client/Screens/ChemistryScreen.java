package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.ChemistryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ChemistryScreen extends AbstractContainerScreen<ChemistryMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/chemistry_gui.png");

    public ChemistryScreen(ChemistryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;
        guiGraphics.blit(BG_LOCATION, x, y, 0, 0, this.imageWidth, this.imageHeight);

        int fuel = this.menu.getScaledFuelProgress();
        if (fuel > 0) {
            int height = 24;
            guiGraphics.blit(BG_LOCATION, x + 151, y + 24 + (height - fuel), 176, 0 + (height - fuel), 14, fuel);
        }

        if (this.menu.isCrafting()) {
            int progress = this.menu.getScaledProgress();
            guiGraphics.blit(BG_LOCATION, x + 76, y + 24, 176, 26, progress, 24);
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        this.renderBg(guiGraphics, partialTicks, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int fuelBarX = this.leftPos + 152;
        int fuelBarY = this.topPos + 24;
        int fuelBarWidth = 14;
        int fuelBarHeight = 24;

        if (mouseX >= fuelBarX && mouseX < fuelBarX + fuelBarWidth &&
                mouseY >= fuelBarY && mouseY < fuelBarY + fuelBarHeight) {
            int currentFuel = this.menu.getFuel();
            int maxFuel = this.menu.getMaxFuel();

            guiGraphics.renderTooltip(this.font,  Component.literal("Fuel: " + currentFuel + " / " + maxFuel), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title.getString(), 50, 6, 0x404040, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle.getString(), 16, this.imageHeight - 96 + 2, 0x404040, false);
    }
}