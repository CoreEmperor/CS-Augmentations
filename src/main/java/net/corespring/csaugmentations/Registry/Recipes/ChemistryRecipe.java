package net.corespring.csaugmentations.Registry.Recipes;

import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSRecipeSerializers;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
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
import net.minecraftforge.common.util.RecipeMatcher;

public class ChemistryRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String group;
    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final int craftingTime;

    public ChemistryRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients, ItemStack result, int craftingTime) {
        this.id = id;
        this.group = group;
        this.ingredients = ingredients;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    @Override
    public boolean matches(Container container, Level level) {
        NonNullList<ItemStack> inputs = NonNullList.create();
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) {
                inputs.add(container.getItem(i));
            }
        }
        return RecipeMatcher.findMatches(inputs, this.ingredients) != null;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= this.ingredients.size();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CSRecipeSerializers.CHEMISTRY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CSRecipeTypes.CHEMISTRY.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(CSBlocks.CHEMISTRY_TABLE.get());
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(Container container) {
        NonNullList<ItemStack> remainders = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < remainders.size(); i++) {
            ItemStack itemstack = container.getItem(i);
            if (!itemstack.isEmpty()) {
                ItemStack remainder = net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(itemstack);
                remainders.set(i, remainder);
            }
        }

        return remainders;
    }
}

