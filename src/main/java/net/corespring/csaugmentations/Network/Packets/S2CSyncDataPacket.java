package net.corespring.csaugmentations.Network.Packets;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Network.CSNetwork;
import net.corespring.csaugmentations.Events.ClientEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class S2CSyncDataPacket {
    private final OrganCap.OrganData organData;
    private final int playerId;

    public S2CSyncDataPacket(OrganCap.OrganData organData, int playerId) {
        this.organData = organData;
        this.playerId = playerId;
    }

    public S2CSyncDataPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readVarInt();
        this.organData = new OrganCap.OrganData(16);
        this.organData.deserializeNBT(buf.readNbt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarInt(playerId);
        buf.writeNbt(this.organData.serializeNBT());
    }

    public OrganCap.OrganData getOrganData() {
        return organData;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null && player.getId() == playerId) {
                    handleCapabilityUpdate(player);
                }
            } else {
                ClientEvents.ClientModBusEvents.updateClientCapability(this);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public void handleCapabilityUpdate(ServerPlayer player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
            data.deserializeNBT(organData.serializeNBT());
            if (player.containerMenu instanceof net.corespring.csaugmentations.Client.Menus.AugmentMenu menu) {
                menu.broadcastChanges();
            }
            CSNetwork.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CSyncDataPacket(data, player.getId()));
        });
    }
}
