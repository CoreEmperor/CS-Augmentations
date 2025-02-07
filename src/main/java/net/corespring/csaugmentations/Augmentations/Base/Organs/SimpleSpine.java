package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public abstract class SimpleSpine extends SimpleOrgan {
    public SimpleSpine(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    public void onActivate(ServerPlayer player) {
    }

    public boolean canActivate(Player player) {
        return false;
    }
}
