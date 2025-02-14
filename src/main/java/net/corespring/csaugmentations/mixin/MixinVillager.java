package net.corespring.csaugmentations.mixin;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class MixinVillager {

    @Inject(method = "startTrading", at = @At("HEAD"), cancellable = true)
    public void onStartTrading(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            LazyOptional<OrganCap.OrganData> capOptional = serverPlayer.getCapability(OrganCap.ORGAN_DATA);
            if (capOptional.isPresent()) {
                OrganCap.OrganData cap = capOptional.orElseThrow(IllegalStateException::new);
                if (cap.isCyberpsycho() && cap.shouldRefuseTrade()) {
                    serverPlayer.displayClientMessage(Component.translatable("message.csaugmentations.villager_refuse_trade"), true);
                    ci.cancel();
                }
            }
        }
    }
}