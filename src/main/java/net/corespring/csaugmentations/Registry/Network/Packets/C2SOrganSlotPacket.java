package net.corespring.csaugmentations.Registry.Network.Packets;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Registry.Network.CSNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class C2SOrganSlotPacket {
    private final int slot;
    private final ItemStack itemStack;

    public C2SOrganSlotPacket(FriendlyByteBuf buf) {
        this.slot = buf.readVarInt();
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(this.slot);
        buf.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
                    data.setStackInSlot(slot, itemStack);
                    CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(data, player.getId()));
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
