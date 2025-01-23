package net.corespring.csaugmentations.Registry.Recipes;

import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class CultivatorRecipe extends CSSingleItemSerializer {
    public CultivatorRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(CSRecipeTypes.CULTIVATING.get(), CSRecipeSerializers.CULTIVATING_SERIALIZER.get(), pId, pGroup, pIngredient, pResult);
    }

    public boolean matches(Container pInv, Level pLevel) {
        return this.ingredient.test(pInv.getItem(0));
    }

    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.CULTIVATOR.get());
    }
}