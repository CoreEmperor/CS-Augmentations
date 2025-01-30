package net.corespring.csaugmentations.Registry.Network;

import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.Network.Packets.C2SOrganSlotPacket;
import net.corespring.csaugmentations.Registry.Network.Packets.C2SToggleArmBuffsPacket;
import net.corespring.csaugmentations.Registry.Network.Packets.C2SToggleLegBuffsPacket;
import net.corespring.csaugmentations.Registry.Network.Packets.S2CSyncDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@EventBusSubscriber(modid = CSAugmentations.MOD_ID)
public class CSNetwork {
    private static final String VERSION = "1.1.1";
    public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CSAugmentations.MOD_ID, "network"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );

    public static void init() {
        int index = 0;

        NETWORK_CHANNEL.messageBuilder(S2CSyncDataPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CSyncDataPacket::toBytes)
                .decoder(S2CSyncDataPacket::new)
                .consumerMainThread(S2CSyncDataPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(C2SOrganSlotPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SOrganSlotPacket::toBytes)
                .decoder(C2SOrganSlotPacket::new)
                .consumerMainThread(C2SOrganSlotPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(C2SToggleArmBuffsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SToggleArmBuffsPacket::toBytes)
                .decoder(C2SToggleArmBuffsPacket::new)
                .consumerMainThread(C2SToggleArmBuffsPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(C2SToggleLegBuffsPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(C2SToggleLegBuffsPacket::toBytes)
                .decoder(C2SToggleLegBuffsPacket::new)
                .consumerMainThread(C2SToggleLegBuffsPacket::handle)
                .add();
    }
}
