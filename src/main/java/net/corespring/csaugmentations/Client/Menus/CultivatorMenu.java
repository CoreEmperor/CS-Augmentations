package net.corespring.csaugmentations.Client.Menus;

import com.google.common.collect.Lists;
import net.corespring.csaugmentations.Registry.CSRecipeTypes;
import net.corespring.csaugmentations.Recipes.CultivatorRecipe;
import net.corespring.csaugmentations.Registry.CSBlocks;
import net.corespring.csaugmentations.Registry.CSMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class CultivatorMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;
    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<CultivatorRecipe> recipes;
    private ItemStack input;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    Runnable slotUpdateListener;
    public final Container container;
    final ResultContainer resultContainer;

    public CultivatorMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf extraData) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public CultivatorMenu(int pContainerId, Inventory pPlayerInventory, final ContainerLevelAccess pAccess) {
        super(CSMenu.CULTIVATOR_MENU.get(), pContainerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = Lists.newArrayList();
        this.input = ItemStack.EMPTY;
        this.slotUpdateListener = () -> {
        };
        this.container = new SimpleContainer(1) {
            public void setChanged() {
                super.setChanged();
                CultivatorMenu.this.slotsChanged(this);
                CultivatorMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.access = pAccess;
        this.level = pPlayerInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            public boolean mayPlace(ItemStack p_40362_) {
                return false;
            }

            public void onTake(Player p_150672_, ItemStack p_150673_) {
                p_150673_.onCraftedBy(p_150672_.level(), p_150672_, p_150673_.getCount());
                ItemStack $$2 = CultivatorMenu.this.inputSlot.remove(1);
                if (!$$2.isEmpty()) {
                    CultivatorMenu.this.setupResultSlot();
                }

                pAccess.execute((p_40364_, p_40365_) -> {
                    long $$3 = p_40364_.getGameTime();
                    if (CultivatorMenu.this.lastSoundTime != $$3) {
                        p_40364_.playSound((Player) null, p_40365_, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        CultivatorMenu.this.lastSoundTime = $$3;
                    }

                });
                super.onTake(p_150672_, p_150673_);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(CultivatorMenu.this.inputSlot.getItem());
            }
        });

        for (int $$3 = 0; $$3 < 3; ++$$3) {
            for (int $$4 = 0; $$4 < 9; ++$$4) {
                this.addSlot(new Slot(pPlayerInventory, $$4 + $$3 * 9 + 9, 8 + $$4 * 18, 84 + $$3 * 18));
            }
        }

        for (int $$5 = 0; $$5 < 9; ++$$5) {
            this.addSlot(new Slot(pPlayerInventory, $$5, 8 + $$5 * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<CultivatorRecipe> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes() {
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, CSBlocks.CULTIVATOR.get());
    }

    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }

        return true;
    }

    private boolean isValidRecipeIndex(int pRecipeIndex) {
        return pRecipeIndex >= 0 && pRecipeIndex < this.recipes.size();
    }

    public void slotsChanged(Container pInventory) {
        ItemStack $$1 = this.inputSlot.getItem();
        if (!$$1.is(this.input.getItem())) {
            this.input = $$1.copy();
            this.setupRecipeList(pInventory, $$1);
        }

    }

    private void setupRecipeList(Container pContainer, ItemStack pStack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!pStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(CSRecipeTypes.CULTIVATING.get(), pContainer, this.level);
        }

    }

    void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            CultivatorRecipe $$0 = (CultivatorRecipe) this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack $$1 = $$0.assemble(this.container, this.level.registryAccess());
            if ($$1.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed($$0);
                this.resultSlot.set($$1);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    public MenuType<?> getType() {
        return CSMenu.CULTIVATOR_MENU.get();
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack $$2 = ItemStack.EMPTY;
        Slot $$3 = (Slot) this.slots.get(pIndex);
        if ($$3 != null && $$3.hasItem()) {
            ItemStack $$4 = $$3.getItem();
            Item $$5 = $$4.getItem();
            $$2 = $$4.copy();
            if (pIndex == 1) {
                $$5.onCraftedBy($$4, pPlayer.level(), pPlayer);
                if (!this.moveItemStackTo($$4, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                $$3.onQuickCraft($$4, $$2);
            } else if (pIndex == 0) {
                if (!this.moveItemStackTo($$4, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.level.getRecipeManager().getRecipeFor(CSRecipeTypes.CULTIVATING.get(), new SimpleContainer(new ItemStack[]{$$4}), this.level).isPresent()) {
                if (!this.moveItemStackTo($$4, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 2 && pIndex < 29) {
                if (!this.moveItemStackTo($$4, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 29 && pIndex < 38 && !this.moveItemStackTo($$4, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if ($$4.isEmpty()) {
                $$3.setByPlayer(ItemStack.EMPTY);
            }

            $$3.setChanged();
            if ($$4.getCount() == $$2.getCount()) {
                return ItemStack.EMPTY;
            }

            $$3.onTake(pPlayer, $$4);
            this.broadcastChanges();
        }

        return $$2;
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((p_40313_, p_40314_) -> this.clearContainer(pPlayer, this.container));
    }
}