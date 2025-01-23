package net.corespring.csaugmentations.Block.BlockEntities;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ChemistrySlots {

    public static class ChemistryFuelSlot extends SlotItemHandler {
        public ChemistryFuelSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == Items.COAL || stack.getItem() == Items.CHARCOAL;
        }
    }

    public static class ChemistryOutputSlot extends SlotItemHandler {

        public ChemistryOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

    }

}
