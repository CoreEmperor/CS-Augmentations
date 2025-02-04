package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Block.BlockEntities.ChemistryBlockEntity;
import net.corespring.csaugmentations.Block.BlockEntities.ChemistrySlots;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ChemistryMenu extends AbstractContainerMenu {
    private final ChemistryBlockEntity blockEntity;
    private final ContainerData data;

    public ChemistryMenu(int id, Inventory playerInventory, ChemistryBlockEntity blockEntity, ContainerData data) {
        super(CSMenu.CHEMISTRY_MENU.get(), id);
        this.blockEntity = blockEntity;
        this.data = data;

        addBlockEntitySlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addDataSlots(data);
    }

    public ChemistryMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, (ChemistryBlockEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()), new SimpleContainerData(4));
    }

    private void addBlockEntitySlots() {
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 0, 16, 17));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 1, 36, 17));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 2, 56, 17));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 3, 16, 36));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 4, 36, 36));
        this.addSlot(new SlotItemHandler(blockEntity.getItemHandler(), 5, 56, 36));
        this.addSlot(new ChemistrySlots.ChemistryOutputSlot(blockEntity.getItemHandler(), 6, 118, 27));
        this.addSlot(new ChemistrySlots.ChemistryFuelSlot(blockEntity.getItemHandler(), 7, 149, 52));
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 8) {
                if (!this.moveItemStackTo(itemstack1, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 8, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, CSBlocks.CHEMISTRY_TABLE.get());
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 34;
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getScaledFuelProgress() {
        int fuel = this.data.get(2);
        int maxFuel = this.data.get(3);
        int fuelIndicatorSize = 24;
        return maxFuel != 0 && fuel != 0 ? fuel * fuelIndicatorSize / maxFuel : 0;
    }

    public int getFuel() {
        return this.data.get(2);
    }

    public int getMaxFuel() {
        return this.data.get(3);
    }
}
