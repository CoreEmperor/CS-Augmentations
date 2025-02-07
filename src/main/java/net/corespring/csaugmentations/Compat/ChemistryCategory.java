package net.corespring.csaugmentations.Compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Recipes.ChemistryRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ChemistryCategory implements IRecipeCategory<ChemistryRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "chemistry");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/chemistry_gui.png");

    public static final RecipeType<ChemistryRecipe> CHEMISTRY = new RecipeType<>(UID, ChemistryRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ChemistryCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlocks.CHEMISTRY_TABLE.get()));
    }

    @Override
    public RecipeType<ChemistryRecipe> getRecipeType() {
        return CHEMISTRY;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.csaugmentations.chemistry_table");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(ChemistryRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.drawCookTime(recipe, guiGraphics, 70);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChemistryRecipe recipe, IFocusGroup focusGroup) {
        int ingredientCount = recipe.getIngredients().size();

        switch (ingredientCount) {
            case 6:
                builder.addSlot(RecipeIngredientRole.INPUT, 56, 36).addIngredients(recipe.getIngredients().get(5));
            case 5:
                builder.addSlot(RecipeIngredientRole.INPUT, 36, 36).addIngredients(recipe.getIngredients().get(4));
            case 4:
                builder.addSlot(RecipeIngredientRole.INPUT, 16, 36).addIngredients(recipe.getIngredients().get(3));
            case 3:
                builder.addSlot(RecipeIngredientRole.INPUT, 56, 17).addIngredients(recipe.getIngredients().get(2));
            case 2:
                builder.addSlot(RecipeIngredientRole.INPUT, 36, 17).addIngredients(recipe.getIngredients().get(1));
            case 1:
                builder.addSlot(RecipeIngredientRole.INPUT, 16, 17).addIngredients(recipe.getIngredients().get(0));
                break;
            default:
                System.out.println("A [CS] Chemistry Recipe appears to be missing an ingredient or two..");
                break;
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 27).addItemStack(recipe.getResultItem(null));
    }

    protected void drawCookTime(ChemistryRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCraftingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, this.getWidth() - stringWidth, y, 0x404040, false);
        }
    }
}
