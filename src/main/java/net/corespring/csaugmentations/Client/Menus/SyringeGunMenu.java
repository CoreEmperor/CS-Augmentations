package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Capability.SyringeGunCap;
import net.corespring.csaugmentations.Item.SyringeGunInjectable;
import net.corespring.csaugmentations.Item.SyringeGunItem;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class SyringeGunMenu extends AbstractContainerMenu {
    private final ItemStack syringeGun;
    private final Player player;

    public SyringeGunMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, playerInv.player.getMainHandItem());
    }

    public SyringeGunMenu(int containerId, Inventory playerInv, FriendlyByteBuf data) {
        this(containerId, playerInv, playerInv.player.getMainHandItem());
    }

    private SyringeGunMenu(int containerId, Inventory playerInv, ItemStack syringeGun) {
        super(CSMenu.SYRINGE_MENU.get(), containerId);
        this.player = playerInv.player;
        this.syringeGun = syringeGun;

        syringeGun.getCapability(SyringeGunCap.SYRINGE_CAP).ifPresent(handler -> {
            addSlot(new SlotItemHandler(handler, 0, 79, 4) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof SyringeGunInjectable;
                }
            });
        });

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 9; y++) {
                this.addSlot(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 84 + x * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemstack = stackInSlot.copy();

            if (index >= 1) {
                if (stackInSlot.getItem() instanceof SyringeGunInjectable) {
                    if (!this.moveItemStackTo(stackInSlot, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index == 0) {
                if (!this.moveItemStackTo(stackInSlot, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        return mainHand.getItem() instanceof SyringeGunItem &&
                !player.isSpectator();
    }
}