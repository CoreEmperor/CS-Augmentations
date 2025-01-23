package net.corespring.csaugmentations.Registry.Recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class CSSimpleCookingSerializer<T extends AbstractCookingRecipe> implements RecipeSerializer<T> {
    private final int defaultCookingTime;
    private final CookieBaker<T> factory;

    public CSSimpleCookingSerializer(CookieBaker<T> factory, int defaultCookingTime) {
        this.defaultCookingTime = defaultCookingTime;
        this.factory = factory;
    }

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject json) {
        String group = GsonHelper.getAsString(json, "group", "");
        CookingBookCategory category = CookingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CookingBookCategory.MISC);
        JsonElement ingredientElement = GsonHelper.isArrayNode(json, "ingredient") ? GsonHelper.getAsJsonArray(json, "ingredient") : GsonHelper.getAsJsonObject(json, "ingredient");
        Ingredient ingredient = Ingredient.fromJson(ingredientElement, false);

        if (!json.has("result")) {
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
        }

        ItemStack result;
        if (json.get("result").isJsonObject()) {
            result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        } else {
            String resultStr = GsonHelper.getAsString(json, "result");
            ResourceLocation resultLocation = new ResourceLocation(resultStr);
            result = new ItemStack(BuiltInRegistries.ITEM.getOptional(resultLocation).orElseThrow(() -> new IllegalStateException("Item: " + resultStr + " does not exist")));
        }

        float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
        int cookingTime = GsonHelper.getAsInt(json, "cookingtime", this.defaultCookingTime);

        return this.factory.create(recipeId, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        String group = buffer.readUtf();
        CookingBookCategory category = buffer.readEnum(CookingBookCategory.class);
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack result = buffer.readItem();
        float experience = buffer.readFloat();
        int cookingTime = buffer.readVarInt();
        return this.factory.create(recipeId, group, category, ingredient, result, experience, cookingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, T recipe) {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeVarInt(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.toNetwork(buffer);
        }

        buffer.writeItem(recipe.getResultItem(null));
        buffer.writeFloat(recipe.getExperience());
        buffer.writeVarInt(recipe.getCookingTime());
    }

    public interface CookieBaker<T extends AbstractCookingRecipe> {
        T create(ResourceLocation recipeId, String group, CookingBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime);
    }
}
