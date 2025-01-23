package net.corespring.csaugmentations.Client.Screens;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Menus.CultivatorMenu;
import net.corespring.csaugmentations.Registry.Recipes.CultivatorRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CultivatorScreen extends AbstractContainerScreen<CultivatorMenu> {
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/cultivator.png");
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public CultivatorScreen(CultivatorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        --this.titleLabelY;
    }

    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(pGuiGraphics);
        int $$4 = this.leftPos;
        int $$5 = this.topPos;
        pGuiGraphics.blit(BG_LOCATION, $$4, $$5, 0, 0, this.imageWidth, this.imageHeight);
        int $$6 = (int) (41.0F * this.scrollOffs);
        pGuiGraphics.blit(BG_LOCATION, $$4 + 119, $$5 + 15 + $$6, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
        int $$7 = this.leftPos + 52;
        int $$8 = this.topPos + 14;
        int $$9 = this.startIndex + 12;
        this.renderButtons(pGuiGraphics, pMouseX, pMouseY, $$7, $$8, $$9);
        this.renderRecipes(pGuiGraphics, $$7, $$8, $$9);
    }

    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
        if (this.displayRecipes) {
            int $$3 = this.leftPos + 52;
            int $$4 = this.topPos + 14;
            int $$5 = this.startIndex + 12;
            List<CultivatorRecipe> $$6 = ((CultivatorMenu) this.menu).getRecipes();

            for (int $$7 = this.startIndex; $$7 < $$5 && $$7 < ((CultivatorMenu) this.menu).getNumRecipes(); ++$$7) {
                int $$8 = $$7 - this.startIndex;
                int $$9 = $$3 + $$8 % 4 * 16;
                int $$10 = $$4 + $$8 / 4 * 18 + 2;
                if (pX >= $$9 && pX < $$9 + 16 && pY >= $$10 && pY < $$10 + 18) {
                    pGuiGraphics.renderTooltip(this.font, ((CultivatorRecipe) $$6.get($$7)).getResultItem(this.minecraft.level.registryAccess()), pX, pY);
                }
            }
        }

    }

    private void renderButtons(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex) {
        for (int $$6 = this.startIndex; $$6 < pLastVisibleElementIndex && $$6 < ((CultivatorMenu) this.menu).getNumRecipes(); ++$$6) {
            int $$7 = $$6 - this.startIndex;
            int $$8 = pX + $$7 % 4 * 16;
            int $$9 = $$7 / 4;
            int $$10 = pY + $$9 * 18 + 2;
            int $$11 = this.imageHeight;
            if ($$6 == ((CultivatorMenu) this.menu).getSelectedRecipeIndex()) {
                $$11 += 18;
            } else if (pMouseX >= $$8 && pMouseY >= $$10 && pMouseX < $$8 + 16 && pMouseY < $$10 + 18) {
                $$11 += 36;
            }

            pGuiGraphics.blit(BG_LOCATION, $$8, $$10 - 1, 0, $$11, 16, 18);
        }

    }

    private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex) {
        List<CultivatorRecipe> $$4 = ((CultivatorMenu) this.menu).getRecipes();

        for (int $$5 = this.startIndex; $$5 < pStartIndex && $$5 < ((CultivatorMenu) this.menu).getNumRecipes(); ++$$5) {
            int $$6 = $$5 - this.startIndex;
            int $$7 = pX + $$6 % 4 * 16;
            int $$8 = $$6 / 4;
            int $$9 = pY + $$8 * 18 + 2;
            pGuiGraphics.renderItem(((CultivatorRecipe) $$4.get($$5)).getResultItem(this.minecraft.level.registryAccess()), $$7, $$9);
        }

    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        if (this.displayRecipes) {
            int $$3 = this.leftPos + 52;
            int $$4 = this.topPos + 14;
            int $$5 = this.startIndex + 12;

            for (int $$6 = this.startIndex; $$6 < $$5; ++$$6) {
                int $$7 = $$6 - this.startIndex;
                double $$8 = pMouseX - (double) ($$3 + $$7 % 4 * 16);
                double $$9 = pMouseY - (double) ($$4 + $$7 / 4 * 18);
                if ($$8 >= (double) 0.0F && $$9 >= (double) 0.0F && $$8 < (double) 16.0F && $$9 < (double) 18.0F && ((CultivatorMenu) this.menu).clickMenuButton(this.minecraft.player, $$6)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    this.minecraft.gameMode.handleInventoryButtonClick(((CultivatorMenu) this.menu).containerId, $$6);
                    return true;
                }
            }

            $$3 = this.leftPos + 119;
            $$4 = this.topPos + 9;
            if (pMouseX >= (double) $$3 && pMouseX < (double) ($$3 + 12) && pMouseY >= (double) $$4 && pMouseY < (double) ($$4 + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int $$5 = this.topPos + 14;
            int $$6 = $$5 + 54;
            this.scrollOffs = ((float) pMouseY - (float) $$5 - 7.5F) / ((float) ($$6 - $$5) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + (double) 0.5F) * 4;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int $$3 = this.getOffscreenRows();
            float $$4 = (float) pDelta / (float) $$3;
            this.scrollOffs = Mth.clamp(this.scrollOffs - $$4, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) $$3) + (double) 0.5F) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && ((CultivatorMenu) this.menu).getNumRecipes() > 12;
    }

    protected int getOffscreenRows() {
        return (((CultivatorMenu) this.menu).getNumRecipes() + 4 - 1) / 4 - 3;
    }

    private void containerChanged() {
        this.displayRecipes = ((CultivatorMenu) this.menu).hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }

    }
}
