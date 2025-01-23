package net.corespring.csaugmentations.Block;

import net.corespring.csaugmentations.Registry.CSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SomniferumCrop extends CSCropBlock {
    public SomniferumCrop(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        int age = getAge(pState);
        if (age == 0 || age == 1 || age == 2) {
            return box(0.0, 0.0, 0.0, 16, 1, 16);
        } else if (age == 3 || age == 4) {
            return box(0.0, 0.0, 0.0, 16, 2, 16);
        } else if (age == 5 || age == 6) {
            return box(0.0, 0.0, 0.0, 16, 5, 16);
        } else if (age == 7) {
            return box(0.0, 0.0, 0.0, 16, 7, 16);
        } else {
            return box(0.0, 0.0, 0.0, 16, 2, 16);
        }
    }

    @NotNull
    @Override
    protected ItemLike getBaseSeedId() {
        return CSItems.SOMNIFERUM_SEEDPOD.get();
    }
}
