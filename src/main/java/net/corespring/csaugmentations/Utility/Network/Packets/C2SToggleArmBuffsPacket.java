package net.corespring.csaugmentations.Utility.Network.Packets;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SToggleArmBuffsPacket {
    private final boolean armsEnabled;

    public C2SToggleArmBuffsPacket(boolean armsEnabled) {
        this.armsEnabled = armsEnabled;
    }

    public C2SToggleArmBuffsPacket(FriendlyByteBuf buf) {
        this.armsEnabled = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.armsEnabled);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                handleToggleArmBuffsPacket(this.armsEnabled, player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void handleToggleArmBuffsPacket(boolean armsEnabled, ServerPlayer player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
            data.applyEffects(armsEnabled);
        });
    }
}
