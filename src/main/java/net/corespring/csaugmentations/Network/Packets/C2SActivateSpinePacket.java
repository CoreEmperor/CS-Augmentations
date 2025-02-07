package net.corespring.csaugmentations.Network.Packets;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleSpine;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SActivateSpinePacket {
    public C2SActivateSpinePacket() {
    }

    public C2SActivateSpinePacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                handleSpineActivation(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void handleSpineActivation(ServerPlayer player) {
        player.getCapability(OrganCap.ORGAN_DATA).ifPresent(data -> {
            ItemStack spineStack = data.getStackInSlot(CSAugUtil.OrganSlots.SPINE);
            if (spineStack.getItem() instanceof SimpleSpine spine) {
                spine.onActivate(player);
            }
        });
    }
}