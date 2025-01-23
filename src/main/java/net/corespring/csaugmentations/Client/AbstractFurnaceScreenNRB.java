package net.corespring.csaugmentations.Client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractFurnaceScreenNRB<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private boolean widthTooNarrow;
    private final ResourceLocation texture;

    public AbstractFurnaceScreenNRB(T pMenu, Inventory pPlayerInventory, Component pTitle, ResourceLocation pTexture) {
        super(pMenu, pPlayerInventory, pTitle);
        this.texture = pTexture;
    }

    public void init() {
        super.init();
        this.widthTooNarrow = this.width < 379;
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.addRenderableWidget(new ImageButton(this.leftPos + 20, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (p_289628_) -> {
        }));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void containerTick() {
        super.containerTick();
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        this.renderBg(pGuiGraphics, pPartialTick, pMouseX, pMouseY);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int $$4 = this.leftPos;
        int $$5 = this.topPos;
        pGuiGraphics.blit(this.texture, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);
        if (this.menu.isLit()) {
            int $$6 = this.menu.getLitProgress();
            pGuiGraphics.blit(this.texture, $$4 + 56, $$5 + 36 + 12 - $$6, 176, 12 - $$6, 14, $$6 + 1);
        }

        int $$7 = this.menu.getBurnProgress();
        pGuiGraphics.blit(this.texture, $$4 + 79, $$5 + 34, 176, 14, $$7 + 1, 16);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        return super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
        boolean $$5 = pMouseX < (double) pGuiLeft || pMouseY < (double) pGuiTop || pMouseX >= (double) (pGuiLeft + this.imageWidth) || pMouseY >= (double) (pGuiTop + this.imageHeight);
        return $$5;
    }

    public boolean charTyped(char pCodePoint, int pModifiers) {
        return super.charTyped(pCodePoint, pModifiers);
    }

    public void recipesUpdated() {
    }
}
