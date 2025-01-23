package net.corespring.csaugmentations.Capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class OrganCapProvider implements ICapabilitySerializable<CompoundTag> {
    private final OrganCap.OrganData organData;
    private final LazyOptional<OrganCap.OrganData> lazyOptional;

    public OrganCapProvider(Player player) {
        this.organData = new OrganCap.OrganData(16);
        this.organData.setPlayer(player);
        this.lazyOptional = LazyOptional.of(() -> organData);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == OrganCap.ORGAN_DATA ? lazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return organData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        organData.deserializeNBT(nbt);
    }
}
