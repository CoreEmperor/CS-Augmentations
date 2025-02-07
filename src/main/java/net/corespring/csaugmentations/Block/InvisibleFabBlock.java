package net.corespring.csaugmentations.Block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class InvisibleFabBlock extends Block {
    public InvisibleFabBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockPos fabPos = findFabBlock(pLevel, pPos);
        if (fabPos != null) {
            BlockState fabState = pLevel.getBlockState(fabPos);
            if (fabState.getBlock() instanceof FabricatorBlock) {
                pLevel.destroyBlock(fabPos, true);
                fabState.getBlock().onRemove(fabState, pLevel, fabPos, pState, false);
            }
        }
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    private BlockPos findFabBlock(Level pLevel, BlockPos pPos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos adjacentPos = pPos.relative(direction);
            BlockState adjacentState = pLevel.getBlockState(adjacentPos);
            if (adjacentState.getBlock() instanceof FabricatorBlock) {
                return adjacentPos;
            }
        }
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState pState, HitResult hitResult, BlockGetter pLevel, BlockPos pPos, Player pPlayer) {
        return ItemStack.EMPTY;
    }
}
