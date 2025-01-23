package net.corespring.csaugmentations.Item.SurgicalEquipment;

import io.netty.buffer.Unpooled;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Client.Menus.AugmentMenu;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class SurgicalEquipmentItem extends Item {
    public SurgicalEquipmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer serverPlayer && pPlayer.hasEffect(CSEffects.INCISION.get())) {
            openMenu(serverPlayer, serverPlayer);
            serverPlayer.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                cap.setPlayer(serverPlayer);
                cap.updateOrganData();
            });
        }
        return InteractionResultHolder.sidedSuccess(pPlayer.getItemInHand(pUsedHand), pLevel.isClientSide());
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pTarget.level().isClientSide && pAttacker instanceof ServerPlayer attackerPlayer && pTarget instanceof ServerPlayer targetPlayer && pTarget.hasEffect(CSEffects.INCISION.get())) {
            openMenu(attackerPlayer, targetPlayer);
            targetPlayer.getCapability(OrganCap.ORGAN_DATA).ifPresent(cap -> {
                cap.setPlayer(targetPlayer);
                cap.updateOrganData();
            });
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    private void openMenu(ServerPlayer opener, Player target) {
        NetworkHooks.openScreen(opener, new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("menu.csaugmentations.augment_menu");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
                buffer.writeUUID(target.getUUID());
                return new AugmentMenu(id, inventory, buffer);
            }
        }, buf -> buf.writeUUID(target.getUUID()));
    }
}
