package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Registry.CSItems;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AugmentMenu extends AbstractContainerMenu {
    public static final int BRAIN_SLOT = 0;
    public static final int EYES_SLOT = 1;
    public static final int RIBS_SLOT = 2;
    public static final int SPINE_SLOT = 3;
    public static final int LEFT_ARM_SLOT = 4;
    public static final int RIGHT_ARM_SLOT = 5;
    public static final int LEFT_LEG_SLOT = 6;
    public static final int RIGHT_LEG_SLOT = 7;
    public static final int LUNGS_SLOT = 8;
    public static final int LEFT_KIDNEY_SLOT = 9;
    public static final int RIGHT_KIDNEY_SLOT = 10;
    public static final int LIVER_SLOT = 11;
    public static final int HEART_SLOT = 12;
    public static final int STOMACH_SLOT = 13;
    public static final int SKIN_SLOT = 14;
    public static final int HEART_UPGRADE_SLOT = 15;

    private final IItemHandlerModifiable itemHandler;
    private final Player player;

    public AugmentMenu(int pContainerId, Inventory pPlayerInv, FriendlyByteBuf pBuf) {
        super(CSMenu.AUGMENT_MENU.get(), pContainerId);
        UUID targetUUID = pBuf.readUUID();
        this.player = pPlayerInv.player.level().getPlayerByUUID(targetUUID);

        if (this.player == null) {
            throw new IllegalArgumentException("Target player does not exist");
        }

        this.itemHandler = OrganCap.getOrganData(this.player);

        addOrganSlots();
        addPlayerInventory(pPlayerInv);
        addPlayerHotbar(pPlayerInv);
    }

    private void addOrganSlots() {
        this.addSlot(createSlot(BRAIN_SLOT, 32, 23));
        this.addSlot(createSlot(EYES_SLOT, 71, 23));
        this.addSlot(createSlot(RIBS_SLOT, 56, 46));
        this.addSlot(createSlot(LEFT_ARM_SLOT, 16, 56));
        this.addSlot(createSlot(RIGHT_ARM_SLOT, 86, 56));
        this.addSlot(createSlot(SPINE_SLOT, 44, 69));
        this.addSlot(createSlot(LEFT_LEG_SLOT, 26, 93));
        this.addSlot(createSlot(RIGHT_LEG_SLOT, 76, 93));
        this.addSlot(createSlot(LUNGS_SLOT, 144, 21));
        this.addSlot(createSlot(LEFT_KIDNEY_SLOT, 139, 43));
        this.addSlot(createSlot(RIGHT_KIDNEY_SLOT, 158, 43));
        this.addSlot(createSlot(STOMACH_SLOT, 144, 81));
        this.addSlot(createSlot(HEART_SLOT, 205, 30));
        this.addSlot(createSlot(HEART_UPGRADE_SLOT, 224, 40));
        this.addSlot(createSlot(LIVER_SLOT, 197, 67));
        this.addSlot(createSlot(SKIN_SLOT, 220, 92));
    }

    private Slot createSlot(int slotIndex, int xPosition, int yPosition) {
        return new SlotItemHandler(itemHandler, slotIndex, xPosition, yPosition);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 9; y++) {
                this.addSlot(new Slot(playerInventory, y + x * 9 + 9, 11 + y * 18, 171 + x * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 11 + x * 18, 229));
        }
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 16) {
                if (!this.moveItemStackTo(itemstack1, 16, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 16, false)) {
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
        if (this.player == null) {
            return false;
        }
        ItemStack pStack = player.getMainHandItem();
        boolean isPlayerAlive = player.isAlive();
        boolean isHoldingCorrectItem = pStack.getItem() == CSItems.RETRACTORS.get();

        return isPlayerAlive && isHoldingCorrectItem;
    }
}
