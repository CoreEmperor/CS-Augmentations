package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import java.util.ArrayList;
import java.util.List;

public class FabricatorSerializer implements RecipeSerializer<FabricatorRecipe> {
    public static final FabricatorSerializer INSTANCE = new FabricatorSerializer();

    @Override
    public FabricatorRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        List<FabricatorRecipe.IngredientWithCount> ingredientList = new ArrayList<>();

        for (int i = 0; i < ingredients.size(); i++) {
            JsonObject ingredientObj = ingredients.get(i).getAsJsonObject();
            Ingredient ingredient = Ingredient.fromJson(ingredientObj.get("ingredient"));
            int count = GsonHelper.getAsInt(ingredientObj, "count", 1);
            ingredientList.add(new FabricatorRecipe.IngredientWithCount(ingredient, count));
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int craftingTime = GsonHelper.getAsInt(json, "craftingTime", 200);

        return new FabricatorRecipe(recipeId, group, ingredientList, result, craftingTime);
    }

    @Override
    public FabricatorRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        int ingredientCount = buffer.readVarInt();
        List<FabricatorRecipe.IngredientWithCount> ingredients = new ArrayList<>();

        for (int i = 0; i < ingredientCount; i++) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int count = buffer.readVarInt();
            ingredients.add(new FabricatorRecipe.IngredientWithCount(ingredient, count));
        }

        ItemStack result = buffer.readItem();
        int craftingTime = buffer.readVarInt();

        return new FabricatorRecipe(recipeId, group, ingredients, result, craftingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, FabricatorRecipe recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeVarInt(recipe.getIngredientCounts().size());

        for (FabricatorRecipe.IngredientWithCount ic : recipe.getIngredientCounts()) {
            ic.ingredient().toNetwork(buffer);
            buffer.writeVarInt(ic.count());
        }

        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeVarInt(recipe.getCraftingTime());
    }
}