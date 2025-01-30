package net.corespring.csaugmentations.Compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Client.Screens.ChemistryScreen;
import net.corespring.csaugmentations.Client.Screens.CultivatorScreen;
import net.corespring.csaugmentations.Client.Screens.RefineryScreen;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.Recipes.ChemistryRecipe;
import net.corespring.csaugmentations.Registry.Recipes.CultivatorRecipe;
import net.corespring.csaugmentations.Registry.Recipes.RefineryRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEICSAugmentationsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CSAugmentations.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CultivatorCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new RefineryCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ChemistryCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CultivatorRecipe> cultivatingrecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.CULTIVATING.get());
        registration.addRecipes(CultivatorCategory.CULTIVATING, cultivatingrecipes);
        List<RefineryRecipe> refiningrecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.REFINING.get());
        registration.addRecipes(RefineryCategory.REFINING, refiningrecipes);
        List<ChemistryRecipe> chemistryrecipes = recipeManager.getAllRecipesFor(CSRecipeTypes.CHEMISTRY.get());
        registration.addRecipes(ChemistryCategory.CHEMISTRY, chemistryrecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CultivatorScreen.class, 76, 30, 28, 23,
                CultivatorCategory.CULTIVATING);
        registration.addRecipeClickArea(RefineryScreen.class, 20, 32, 20, 20,
                RefineryCategory.REFINING);
        registration.addRecipeClickArea(ChemistryScreen.class, 76, 24, 34, 18,
                ChemistryCategory.CHEMISTRY);
    }

}
