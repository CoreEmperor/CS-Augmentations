package net.corespring.csaugmentations.Recipes;

import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import java.util.ArrayList;
import java.util.List;

public class FabricatorRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final List<IngredientWithCount> ingredients;
    private final ItemStack result;
    private final int craftingTime;

    public record IngredientWithCount(Ingredient ingredient, int count) {}

    public FabricatorRecipe(ResourceLocation id, String group, List<IngredientWithCount> ingredients, ItemStack result, int craftingTime) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    public List<IngredientWithCount> getIngredientCounts() {
        return ingredients;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            inputs.add(container.getItem(i));
        }

        for (IngredientWithCount ingredient : ingredients) {
            int total = 0;
            for (ItemStack stack : inputs) {
                if (ingredient.ingredient().test(stack)) {
                    total += stack.getCount();
                }
            }
            if (total < ingredient.count()) return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CSRecipeSerializers.FABRICATOR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (IngredientWithCount ic : ingredients) {
            list.add(ic.ingredient());
        }
        return list;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.FABRICATOR.get());
    }

    @Override
    public String getGroup() {
        return group;
    }

    public static class Type implements RecipeType<FabricatorRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "fabricator";
    }
}