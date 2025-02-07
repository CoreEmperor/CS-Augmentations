package net.corespring.csaugmentations.Block.BlockEntities;

import net.corespring.csaugmentations.Client.Menus.FabricatorMenu;
import net.corespring.csaugmentations.Registry.CSBlockEntities;
import net.corespring.csaugmentations.Recipes.FabricatorRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;
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

import java.util.*;
import java.util.stream.Collectors;

public class FabricatorBlockEntity extends BlockEntity implements MenuProvider {
    public static final int INPUT_SLOTS = 8;
    public static final int BLUEPRINT_SLOT = 8;
    public static final int OUTPUT_SLOT = 9;
    public static final int TOTAL_SLOTS = 10;

    private FabricatorRecipe lastRecipe;
    protected final ContainerData data;
    private final ItemStackHandler itemHandler = new ItemStackHandler(TOTAL_SLOTS);
    private int progress = 0;
    private int maxProgress = 22;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public FabricatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntities.FABRICATOR_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> itemHandler.getStackInSlot(BLUEPRINT_SLOT).getCount();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 1 -> maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.csaugmentations.fabricator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new FabricatorMenu(i, inventory, this, this.data);
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
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("fabricator.progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("fabricator.progress");
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide) return;

        if (!hasBlueprint()) {
            resetProgress();
            return;
        }


        Optional<FabricatorRecipe> currentRecipe = getCurrentRecipe();
        boolean hasValidRecipe = currentRecipe.isPresent() && canCraft(currentRecipe.get());

        if (!hasValidRecipe || !isSameRecipeAsBefore(currentRecipe.get())) {
            resetProgress();
            return;
        }

        FabricatorRecipe recipe = currentRecipe.get();
        this.maxProgress = recipe.getCraftingTime();

        if (hasProgressFinished()) {
            craftItem(recipe);
            consumeBlueprint();
            resetProgress();
        } else {
            increaseCraftingProgress();
        }
        setChanged(level, pos, state);
    }

    private Optional<FabricatorRecipe> getCurrentRecipe() {
        if (level == null) return Optional.empty();

        List<FabricatorRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(FabricatorRecipe.Type.INSTANCE)
                .stream()
                .sorted((a, b) -> {
                    int countCompare = Integer.compare(
                            b.getIngredientCounts().size(),
                            a.getIngredientCounts().size()
                    );
                    return countCompare != 0 ? countCompare : Integer.compare(
                            a.getCraftingTime(),
                            b.getCraftingTime()
                    );
                })
                .toList();

        for (FabricatorRecipe recipe : recipes) {
            if (canCraft(recipe)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    private boolean canCraft(FabricatorRecipe recipe) {
        if (!hasBlueprint()) {
            return false;
        }

        Map<Ingredient, Integer> required = recipe.getIngredientCounts().stream()
                .collect(Collectors.toMap(
                        FabricatorRecipe.IngredientWithCount::ingredient,
                        FabricatorRecipe.IngredientWithCount::count
                ));

        Map<Ingredient, Integer> available = new HashMap<>();
        for (int i = 0; i < INPUT_SLOTS; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                for (Ingredient ingredient : required.keySet()) {
                    if (ingredient.test(stack)) {
                        available.put(ingredient, available.getOrDefault(ingredient, 0) + stack.getCount());
                    }
                }
            }
        }

        ItemStack result = recipe.getResultItem(level.registryAccess());
        ItemStack outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (!outputStack.isEmpty() && !ItemStack.isSameItemSameTags(outputStack, result)) {
            return false;
        }
        if (outputStack.getCount() + result.getCount() > outputStack.getMaxStackSize()) {
            return false;
        }

        return required.entrySet().stream()
                .allMatch(entry -> available.getOrDefault(entry.getKey(), 0) >= entry.getValue());
    }

    private void craftItem(FabricatorRecipe recipe) {
        if (!canCraft(recipe)) return;

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < INPUT_SLOTS; i++) {
            inputs.add(itemHandler.getStackInSlot(i));
        }

        for (FabricatorRecipe.IngredientWithCount ingredient : recipe.getIngredientCounts()) {
            int remaining = ingredient.count();
            for (int i = 0; i < INPUT_SLOTS && remaining > 0; i++) {
                ItemStack stack = inputs.get(i);
                if (ingredient.ingredient().test(stack)) {
                    int deduct = Math.min(remaining, stack.getCount());
                    stack.shrink(deduct);
                    remaining -= deduct;
                    itemHandler.setStackInSlot(i, stack);
                }
            }
        }

        ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
        ItemStack outputSlot = itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (outputSlot.isEmpty()) {
            itemHandler.setStackInSlot(OUTPUT_SLOT, output);
        } else {
            outputSlot.grow(output.getCount());
        }
    }

    private boolean hasBlueprint() {
        return !itemHandler.getStackInSlot(BLUEPRINT_SLOT).isEmpty();
    }

    private void consumeBlueprint() {
        ItemStack blueprint = itemHandler.getStackInSlot(BLUEPRINT_SLOT);
        if (!blueprint.isEmpty()) {
            blueprint.shrink(1);
        }
    }

    private boolean isSameRecipeAsBefore(FabricatorRecipe newRecipe) {
        if (lastRecipe == null) {
            lastRecipe = newRecipe;
            return true;
        }
        boolean isSame = lastRecipe.getId().equals(newRecipe.getId());
        lastRecipe = newRecipe;
        return isSame;
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void resetProgress() {
        if (progress > 0) {
            progress = 0;
            setChanged();
        }
    }
}