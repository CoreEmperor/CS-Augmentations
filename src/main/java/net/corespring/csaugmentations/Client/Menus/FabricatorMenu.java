package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.FabricatorBlockEntity;
import net.corespring.csaugmentations.Block.BlockEntities.FabricatorSlots;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FabricatorMenu extends AbstractContainerMenu {
    private final FabricatorBlockEntity blockEntity;
    private final ContainerData data;

    public FabricatorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public FabricatorMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(CSMenu.FABRICATOR_MENU.get(), id);
        this.blockEntity = (FabricatorBlockEntity) entity;
        this.data = data;

        IItemHandler handler = blockEntity.getItemHandler();

        addSlot(new SlotItemHandler(handler, 0, 16, 17));
        addSlot(new SlotItemHandler(handler, 1, 35, 17));
        addSlot(new SlotItemHandler(handler, 2, 54, 17));
        addSlot(new SlotItemHandler(handler, 3, 73, 17));
        addSlot(new SlotItemHandler(handler, 4, 16, 36));
        addSlot(new SlotItemHandler(handler, 5, 35, 36));
        addSlot(new SlotItemHandler(handler, 6, 54, 36));
        addSlot(new SlotItemHandler(handler, 7, 73, 36));
        addSlot(new FabricatorSlots.FabricatorBlueprintSlot(handler, 8, 172, 33));
        addSlot(new FabricatorSlots.FabricatorOutputSlot(handler, 9, 114, 55));

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    public int getProgress() {
        return data.get(0);
    }

    public int getMaxProgress() {
        return data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 9) {
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index == 9) {
                if (!this.moveItemStackTo(itemstack1, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (isBlueprint(itemstack1)) {
                    if (!this.moveItemStackTo(itemstack1, 8, 9, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, 0, 8, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private boolean isBlueprint(ItemStack stack) {
        return stack.getItem() == CSItems.BLUEPRINT.get();
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, CSBlocks.FABRICATOR.get());
    }
}