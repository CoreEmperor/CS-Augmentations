package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.RefineryMenu;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RefineryBlockEntity extends AbstractFurnaceBlockEntity {
    public RefineryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntities.REFINERY_BE.get(), pPos, pBlockState, CSRecipeTypes.REFINING.get());
    }

    protected Component getDefaultName() {
        return Component.translatable("block.csaugmentations.refinery");
    }

    protected AbstractContainerMenu createMenu(int pId, Inventory pPlayer) {
        return new RefineryMenu(pId, pPlayer, this, this.dataAccess);
    }
}
