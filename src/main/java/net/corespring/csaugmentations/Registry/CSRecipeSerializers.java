package net.corespring.csaugmentations.Registry;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.Recipes.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CSRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CSAugmentations.MOD_ID);

    public static final RegistryObject<RecipeSerializer<CultivatorRecipe>> CULTIVATING_SERIALIZER =
            SERIALIZERS.register("cultivating", () -> new CSSingleItemSerializer.Serializer<>(CultivatorRecipe::new));

    public static final RegistryObject<RecipeSerializer<RefineryRecipe>> REFINING_SERIALIZER =
            SERIALIZERS.register("refining", () -> new CSSimpleCookingSerializer<>(RefineryRecipe::new, 200));

    public static final RegistryObject<RecipeSerializer<ChemistryRecipe>> CHEMISTRY_SERIALIZER =
            SERIALIZERS.register("chemistry", () -> CSChemistrySerializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
