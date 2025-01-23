package net.corespring.csaugmentations.DataGen;


import net.corespring.csaugmentations.CSAugmentations;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class CSGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public CSGlobalLootModifiersProvider(PackOutput output) {
        super(output, CSAugmentations.MOD_ID);
    }

    @Override
    protected void start() {

        /*
        add("flesh_from_villager", new AddItemModifier(new LootItemCondition[]{
                new LootTableIdCondition.Builder(new ResourceLocation("entities/villager")).build(),
                LootItemRandomChanceCondition.randomChance(0.70f).build()}, CSItems.FLESH.get()));

         */
    }

}

