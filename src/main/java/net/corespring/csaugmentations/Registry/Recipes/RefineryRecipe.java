package net.corespring.csaugmentations.Registry.Recipes;

import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class RefineryRecipe extends AbstractCookingRecipe {
    public RefineryRecipe(ResourceLocation pId, String pGroup, CookingBookCategory pCategory, Ingredient pIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        super(CSRecipeTypes.REFINING.get(), pId, pGroup, pCategory, pIngredient, pResult, pExperience, pCookingTime);
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.REFINERY.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return CSRecipeSerializers.REFINING_SERIALIZER.get();
    }
}