package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FabricatorSlots {

    public static class FabricatorBlueprintSlot extends SlotItemHandler {
        public FabricatorBlueprintSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() == CSItems.BLUEPRINT.get();
        }
    }

    public static class FabricatorOutputSlot extends SlotItemHandler {

        public FabricatorOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

    }

}
