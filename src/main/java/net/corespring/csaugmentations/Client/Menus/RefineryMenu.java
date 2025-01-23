package net.corespring.csaugmentations.Client.Menus;

import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;

public class RefineryMenu extends AbstractFurnaceMenu {

    public RefineryMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        super(CSMenu.REFINERY_MENU.get(), CSRecipeTypes.REFINING.get(), null, pContainerId, pPlayerInventory);
    }

    public RefineryMenu(int pContainerId, Inventory pPlayerInventory, Container pFurnaceContainer, ContainerData pFurnaceData) {
        super(CSMenu.REFINERY_MENU.get(), CSRecipeTypes.REFINING.get(), null, pContainerId, pPlayerInventory, pFurnaceContainer, pFurnaceData);
    }
}
