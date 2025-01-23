package net.corespring.csaugmentations.Block;

import net.corespring.csaugmentations.Block.BlockEntities.RefineryBlockEntity;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RefineryBlock extends AbstractFurnaceBlock {
    public RefineryBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void openContainer(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity $$3 = pLevel.getBlockEntity(pPos);
        if ($$3 instanceof RefineryBlockEntity) {
            pPlayer.openMenu((MenuProvider) $$3);
        }
    }

    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RefineryBlockEntity(pPos, pState);
    }

    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createFurnaceTicker(pLevel, pBlockEntityType, CSBlockEntities.REFINERY_BE.get());
    }
}
