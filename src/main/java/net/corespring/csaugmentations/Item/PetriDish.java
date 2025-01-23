package net.corespring.csaugmentations.Item;

import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PetriDish extends Item {
    public PetriDish(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(CSItems.CRUDE_PETRI_DISH.get());
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
