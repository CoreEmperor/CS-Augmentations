package net.corespring.csaugmentations.Network.Packets;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SToggleLegBuffsPacket {
    private final boolean legsEnabled;

    public C2SToggleLegBuffsPacket(boolean legsEnabled) {
        this.legsEnabled = legsEnabled;
    }

    public C2SToggleLegBuffsPacket(FriendlyByteBuf buf) {
        this.legsEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.legsEnabled);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                handleToggleLegBuffsPacket(this.legsEnabled, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void handleToggleLegBuffsPacket(boolean legsEnabled, ServerPlayer player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
            data.applyEffects(legsEnabled);
        });
    }
}
