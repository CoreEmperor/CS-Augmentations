package net.corespring.csaugmentations.Augmentations.Base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IOrganTickable {
    void organTick(ItemStack stack, Level level, Player player, int slotId);
}
