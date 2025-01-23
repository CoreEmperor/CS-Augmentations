package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.ChemistryMenu;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Registry.Recipes.ChemistryRecipe;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChemistryBlockEntity extends BlockEntity implements MenuProvider {
    private static final int OUTPUT_SLOT = 6;
    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(8);
    private int coal_stored = 0;
    private int max_coal_stored = 64;
    private int progress = 0;
    private int maxProgress = 34;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ChemistryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntities.CHEMISTRY_TABLE_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> ChemistryBlockEntity.this.progress;
                    case 1 -> ChemistryBlockEntity.this.maxProgress;
                    case 2 -> ChemistryBlockEntity.this.coal_stored;
                    case 3 -> ChemistryBlockEntity.this.max_coal_stored;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> ChemistryBlockEntity.this.progress = i1;
                    case 1 -> ChemistryBlockEntity.this.maxProgress = i1;
                    case 2 -> ChemistryBlockEntity.this.coal_stored = i1;
                    case 3 -> ChemistryBlockEntity.this.max_coal_stored = i1;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.chemistry_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ChemistryMenu(i, inventory, this, this.data);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);

        ItemStack stack = new ItemStack(this.getBlockState().getBlock().asItem());
        CompoundTag chemStoredData = new CompoundTag();
        saveAdditional(chemStoredData);
        stack.addTagElement("chemStoredData", chemStoredData);
        Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), stack);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("chemistry_table.progress", progress);
        pTag.putInt("chemistry_table.coal_stored", coal_stored);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("chemistry_table.progress");
        coal_stored = pTag.getInt("chemistry_table.coal_stored");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (coal_stored < max_coal_stored) {
            consumeFuel();
        }

        Optional<ChemistryRecipe> match = hasRecipe();
        if (match.isPresent() && coal_stored > 0) {
            ChemistryRecipe recipe = match.get();
            this.maxProgress = recipe.getCraftingTime();

            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
                coal_stored--;
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private Optional<ChemistryRecipe> hasRecipe() {
        Level level = this.level;
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        Optional<ChemistryRecipe> match = level.getRecipeManager().getRecipeFor(CSRecipeTypes.CHEMISTRY.get(), inventory, level);
        return match.isPresent() && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem(level.registryAccess())) ? match : Optional.empty();
    }

    private boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack result) {
        ItemStack outputStack = inventory.getItem(6);
        return outputStack.isEmpty() || (outputStack.getItem() == result.getItem() && outputStack.getCount() + result.getCount() <= outputStack.getMaxStackSize());
    }

    private void craftItem() {
        SimpleContainer inventory = prepareInventory();
        Optional<ChemistryRecipe> match = findRecipe(inventory);

        if (match.isPresent()) {
            ChemistryRecipe recipe = match.get();
            processOutput(recipe);
            consumeInputItems();
            setChanged();
        }
    }

    private SimpleContainer prepareInventory() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        return inventory;
    }

    private Optional<ChemistryRecipe> findRecipe(SimpleContainer inventory) {
        Level level = this.level;
        return level.getRecipeManager().getRecipeFor(CSRecipeTypes.CHEMISTRY.get(), inventory, level);
    }

    private void processOutput(ChemistryRecipe recipe) {
        Level level = this.level;
        ItemStack result = recipe.getResultItem(level.registryAccess()).copy();
        ItemStack outputStack = this.itemHandler.getStackInSlot(OUTPUT_SLOT);

        if (outputStack.isEmpty()) {
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, result);
        } else if (outputStack.getItem() == result.getItem() &&
                outputStack.getCount() + result.getCount() <= outputStack.getMaxStackSize()) {
            outputStack.grow(result.getCount());
        }
    }

    private void consumeInputItems() {
        for (int i = 0; i < 6; i++) {
            ItemStack slotStack = this.itemHandler.getStackInSlot(i);
            if (!slotStack.isEmpty()) {
                ItemStack remainder = net.minecraftforge.common.ForgeHooks.getCraftingRemainingItem(slotStack);
                slotStack.shrink(1);

                if (!remainder.isEmpty()) {
                    boolean placed = false;
                    for (int j = 0; j < this.itemHandler.getSlots(); j++) {
                        ItemStack existingStack = this.itemHandler.getStackInSlot(j);
                        if (existingStack.isEmpty()) {
                            this.itemHandler.setStackInSlot(j, remainder);
                            placed = true;
                            break;
                        } else if (existingStack.getItem() == remainder.getItem() &&
                                existingStack.getCount() + remainder.getCount() <= existingStack.getMaxStackSize()) {
                            existingStack.grow(remainder.getCount());
                            placed = true;
                            break;
                        }
                    }
                    if (!placed) {
                        Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), remainder);
                    }
                }
            }
        }
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void consumeFuel() {
        ItemStack fuelStack = itemHandler.getStackInSlot(7);
        if (!fuelStack.isEmpty() && (fuelStack.getItem() == Items.COAL || fuelStack.getItem() == Items.CHARCOAL)) {
            if (coal_stored < max_coal_stored && fuelStack.getCount() > 0) {
                coal_stored++;
                fuelStack.shrink(1);
                setChanged();
            }
        }

    }
}


