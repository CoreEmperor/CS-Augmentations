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
import net.corespring.csaugmentations.Recipes.FabricatorRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FabricatorCategory implements IRecipeCategory<FabricatorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CSAugmentations.MOD_ID, "fabricator");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CSAugmentations.MOD_ID, "textures/gui/fabricator_gui.png");

    public static final RecipeType<FabricatorRecipe> FABRICATING = new RecipeType<>(UID, FabricatorRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public FabricatorCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 208, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CSBlocks.FABRICATOR.get()));
    }

    @Override
    public RecipeType<FabricatorRecipe> getRecipeType() {
        return FABRICATING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.csaugmentations.fabricator");
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
    public void setRecipe(IRecipeLayoutBuilder builder, FabricatorRecipe recipe, IFocusGroup focusGroup) {
        int ingredientCount = recipe.getIngredientCounts().size();

        switch (ingredientCount) {
            case 8:
                addSlotWithCount(builder, recipe, 7, 73, 36);
            case 7:
                addSlotWithCount(builder, recipe, 6, 54, 36);
            case 6:
                addSlotWithCount(builder, recipe, 5, 35, 36);
            case 5:
                addSlotWithCount(builder, recipe, 4, 16, 36);
            case 4:
                addSlotWithCount(builder, recipe, 3, 73, 17);
            case 3:
                addSlotWithCount(builder, recipe, 2, 54, 17);
            case 2:
                addSlotWithCount(builder, recipe, 1, 35, 17);
            case 1:
                addSlotWithCount(builder, recipe, 0, 16, 17);
                break;
            default:
                System.out.println("A [CS] Fabricator Recipe has an invalid number of ingredients: " + ingredientCount);
                break;
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 172, 33).addItemStack(new ItemStack(CSItems.BLUEPRINT.get()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 114, 55).addItemStack(recipe.getResultItem(null));
    }

    private void addSlotWithCount(IRecipeLayoutBuilder builder, FabricatorRecipe recipe, int index, int x, int y) {
        var ingredient = recipe.getIngredientCounts().get(index);
        builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addIngredients(ingredient.ingredient())
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.literal("Amount Required: " + ingredient.count()));
                });
    }

    @Override
    public void draw(FabricatorRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.drawCraftingTime(recipe, guiGraphics, 70);
    }

    protected void drawCraftingTime(FabricatorRecipe recipe, GuiGraphics guiGraphics, int y) {
        int craftingTime = recipe.getCraftingTime();
        if (craftingTime > 0) {
            int craftingTimeSeconds = craftingTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", craftingTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, this.getWidth() - stringWidth, y, 0x404040, false);
        }
    }
}