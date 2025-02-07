package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class CSChemistrySerializer implements RecipeSerializer<ChemistryRecipe> {
    public static final CSChemistrySerializer INSTANCE = new CSChemistrySerializer();

    @Override
    public ChemistryRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        JsonArray ingredientsJson = GsonHelper.getAsJsonArray(json, "ingredients");
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (int i = 0; i < ingredientsJson.size(); i++) {
            ingredients.add(Ingredient.fromJson(ingredientsJson.get(i)));
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int craftingTime = GsonHelper.getAsInt(json, "craftingTime", 200);

        return new ChemistryRecipe(recipeId, group, ingredients, result, craftingTime);
    }

    @Override
    public ChemistryRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        int ingredientCount = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buffer));
        }

        ItemStack result = buffer.readItem();
        int craftingTime = buffer.readVarInt();

        return new ChemistryRecipe(recipeId, group, ingredients, result, craftingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ChemistryRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeVarInt(recipe.getCraftingTime());
    }
}
