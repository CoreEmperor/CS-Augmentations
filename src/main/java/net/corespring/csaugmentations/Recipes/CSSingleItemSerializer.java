package net.corespring.csaugmentations.Recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class CSSingleItemSerializer extends SingleItemRecipe {

    public CSSingleItemSerializer(RecipeType<?> pType, RecipeSerializer<?> pSerializer, ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(pType, pSerializer, pId, pGroup, pIngredient, pResult);
    }

    @Override
    public boolean matches(Container container, Level level) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (ingredient.test(container.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    public static class Serializer<T extends SingleItemRecipe> implements RecipeSerializer<T> {
        final SingleItemMaker<T> factory;

        public Serializer(SingleItemMaker<T> pFactory) {
            this.factory = pFactory;
        }

        @Override
        public T fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            String group = GsonHelper.getAsString(pJson, "group", "");
            Ingredient ingredient;
            if (GsonHelper.isArrayNode(pJson, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(pJson, "ingredient"), false);
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(pJson, "ingredient"), false);
            }

            String resultString = GsonHelper.getAsString(pJson, "result");
            int count = GsonHelper.getAsInt(pJson, "count");
            ItemStack result = new ItemStack((ItemLike) BuiltInRegistries.ITEM.get(new ResourceLocation(resultString)), count);
            return this.factory.create(pRecipeId, group, ingredient, result);
        }

        @Override
        public T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            String group = pBuffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            return this.factory.create(pRecipeId, group, ingredient, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
            pBuffer.writeUtf(pRecipe.getGroup());
            pRecipe.getIngredients().get(0).toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.getResultItem(null));
        }

        public interface SingleItemMaker<T extends SingleItemRecipe> {
            T create(ResourceLocation var1, String var2, Ingredient var3, ItemStack var4);
        }
    }
}
