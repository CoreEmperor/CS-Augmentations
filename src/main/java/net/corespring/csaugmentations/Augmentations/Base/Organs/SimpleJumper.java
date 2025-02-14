package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleJumper extends SimpleSpine {
    private final double jumpHeight;
    private final long cooldown;
    private final float exhaustion;
    private boolean hasCyberEyes;

    public SimpleJumper(IOrganTiers pTier, Properties pProperties, double pJumpHeight, long pCooldown, float pExhaustion) {
        super(pTier, pProperties);
        this.jumpHeight = pJumpHeight;
        this.cooldown = pCooldown * 50L;
        this.exhaustion = pExhaustion;
    }

    @Override
    public void onActivate(ServerPlayer player) {
        if (!hasRequiredCyberware(player)) {
            sendMissingCyberwareMessage(player);
            return;
        }

        if (!canActivate(player)) {
            sendCooldownMessage(player);
            return;
        }

        performSuperJump(player);
    }

    @Override
    public boolean canActivate(Player player) {
        CompoundTag data = player.getPersistentData();
        long lastUsed = data.getLong("JumperLastUsed");
        return System.currentTimeMillis() - lastUsed > cooldown;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.csaugmentations.jumper.warning")
                .withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.literal(String.format("%.1f", jumpHeight))
                .append(Component.translatable("tooltip.csaugmentations.jumper.height"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.literal((cooldown / 1000) + "s")
                .append(Component.translatable("tooltip.csaugmentations.jumper.cooldown"))
                .withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.literal(exhaustion + "")
                .append(Component.translatable("tooltip.csaugmentations.jumper.exhaustion"))
                .withStyle(ChatFormatting.DARK_RED));
    }

    private void performSuperJump(ServerPlayer player) {
        if (player.onGround()) {
            Vec3 motion = player.getDeltaMovement();

            player.setDeltaMovement(motion.x, jumpHeight, motion.z);
            player.hurtMarked = true;

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.0F);

            applyCooldown(player);
            applyExhaustion(player);
        } else {
            if (!hasCyberEyes) {
                return;
            }
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.jumper.not_on_ground")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
    }


    private void applyCooldown(Player player) {
        player.getPersistentData().putLong("JumperLastUsed", System.currentTimeMillis());
    }

    private void applyExhaustion(Player player) {
        player.causeFoodExhaustion(exhaustion);
    }

    private void sendCooldownMessage(ServerPlayer player) {
        if (!hasCyberEyes) {
            return;
        }

        long lastUsed = player.getPersistentData().getLong("JumperLastUsed");
        long remainingCooldown = cooldown - (System.currentTimeMillis() - lastUsed);

        if (remainingCooldown > 0) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.jumper.cooldown")
                            .append(" " + (remainingCooldown / 1000) + "s")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    private void sendMissingCyberwareMessage(ServerPlayer player) {
        OrganCap.OrganData data = OrganCap.getOrganData(player);
        if (data == null) return;

        if (!hasCyberEyes) {
            return;
        }

        boolean hasCyberbrain = !data.getStackInSlot(CSAugUtil.OrganSlots.BRAIN).isEmpty() &&
                data.isTierAboveProsthetic(CSAugUtil.OrganSlots.BRAIN);

        if (!hasCyberbrain) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.missing_brain")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
        else if (!data.isTierAboveProsthetic(CSAugUtil.OrganSlots.RIBS)) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.missing_ribs")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    private boolean hasRequiredCyberware(Player player) {
        OrganCap.OrganData data = OrganCap.getOrganData(player);
        if (data == null) return false;

        return data.isTierAboveProsthetic(CSAugUtil.OrganSlots.BRAIN) &&
                data.isTierAboveProsthetic(CSAugUtil.OrganSlots.RIBS);
    }

    private boolean checkCyberEyes(ServerPlayer player) {
        OrganCap.OrganData data = OrganCap.getOrganData(player);
        if (data == null) return false;

        hasCyberEyes = !data.getStackInSlot(CSAugUtil.OrganSlots.EYES).isEmpty() &&
                data.isTierAboveProsthetic(CSAugUtil.OrganSlots.EYES);

        return hasCyberEyes;
    }

}
