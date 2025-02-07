package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleWarper extends SimpleSpine {
    private final int teleportDistance;
    private final long cooldown;
    private final float exhaustion;
    private final int chargeTime;

    public SimpleWarper(IOrganTiers pTier, Properties pProperties, int pTeleportDistance, long pCooldown, float pExhaustion, int pChargeTime) {
        super(pTier, pProperties);
        this.teleportDistance = pTeleportDistance;
        this.cooldown = pCooldown * 50L;
        this.exhaustion = pExhaustion;
        this.chargeTime = pChargeTime;
    }

    @Override
    public void applyEffects(Player pPlayer) {
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

        startCharge(player);
    }

    @Override
    public void organTick(ItemStack pStack, Level pLevel, Player pPlayer, int pSlotId) {
        if (pPlayer instanceof ServerPlayer sPlayer) {
            sPlayer.getCapability(OrganCap.ORGAN_DATA).ifPresent(organData -> {
                ItemStack spineStack = organData.getStackInSlot(CSAugUtil.OrganSlots.SPINE);
                if (spineStack.getItem() instanceof SimpleWarper simpleWarper) {
                    simpleWarper.handleChargeTick(sPlayer);
                }
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal(teleportDistance + "m")
                .append(Component.translatable("tooltip.csaugmentations.warper.distance"))
                .withStyle(ChatFormatting.BLUE));
        pTooltipComponents.add(Component.literal((cooldown / 1000) + "s")
                .append(Component.translatable("tooltip.csaugmentations.warper.cooldown"))
                .withStyle(ChatFormatting.RED));
        pTooltipComponents.add(Component.literal((chargeTime / 20) + "s")
                .append(Component.translatable("tooltip.csaugmentations.warper.charge"))
                .withStyle(ChatFormatting.YELLOW));
        pTooltipComponents.add(Component.literal(exhaustion + "")
                .append(Component.translatable("tooltip.csaugmentations.warper.exhaustion"))
                .withStyle(ChatFormatting.DARK_RED));
    }

    @Override
    public boolean canActivate(Player player) {
        CompoundTag data = player.getPersistentData();
        long lastUsed = data.getLong("WarperLastUsed");
        boolean isCharging = data.getBoolean("WarperCharging");

        return !isCharging && (System.currentTimeMillis() - lastUsed > cooldown);
    }

    private void startCharge(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putLong("WarperChargeStart", player.level().getGameTime());
        data.putBoolean("WarperCharging", true);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BEACON_POWER_SELECT, SoundSource.PLAYERS, 1.0F, 0.5F);
    }

    public void handleChargeTick(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (data.getBoolean("WarperCharging")) {
            long chargeStart = data.getLong("WarperChargeStart");
            long currentTime = player.level().getGameTime();

            spawnChargeParticles(player);

            if (currentTime - chargeStart >= chargeTime) {
                completeCharge(player);
            }
        }
    }

    private void completeCharge(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove("WarperChargeStart");
        data.remove("WarperCharging");

        Vec3 newPos = calculateTeleportPosition(player);

        if (isSafeLocation(player, newPos)) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

            BlockPos groundPos = BlockPos.containing(newPos).below();
            if (player.level().getBlockState(groundPos).isSolid()) {
                newPos = new Vec3(newPos.x, groundPos.getY() + 1.0d, newPos.z);
            }

            player.teleportTo(newPos.x, newPos.y, newPos.z);
            spawnPostTeleportParticles(player);
            applyCooldown(player);
            applyExhaustion(player);
        }
    }

    private Vec3 calculateTeleportPosition(ServerPlayer player) {
        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 viewVec = player.getViewVector(1.0f);
        Vec3 endVec = eyePos.add(viewVec.scale(teleportDistance));

        ClipContext context = new ClipContext(eyePos, endVec,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        BlockHitResult hitResult = player.level().clip(context);

        return hitResult.getType() == HitResult.Type.BLOCK ?
                hitResult.getLocation() : endVec;
    }

    private void spawnChargeParticles(ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.position();

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 3; i++) {
                double x = pos.x + (level.random.nextDouble() - 0.5) * 1.5;
                double y = pos.y + player.getEyeHeight();
                double z = pos.z + (level.random.nextDouble() - 0.5) * 1.5;

                serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 1, 0, 0, 0, 0.1);
            }
        }
    }

    private void spawnPostTeleportParticles(ServerPlayer player) {
        Level level = player.level();
        Vec3 pos = player.position();

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 10; i++) {
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                        pos.x + (level.random.nextDouble() - 0.5) * 2,
                        pos.y + level.random.nextDouble(),
                        pos.z + (level.random.nextDouble() - 0.5) * 2,
                        1, 0, 0, 0, 0.1);
            }
        }
    }

    private void sendCooldownMessage(ServerPlayer player) {
        long lastUsed = player.getPersistentData().getLong("WarperLastUsed");
        long remainingCooldown = cooldown - (System.currentTimeMillis() - lastUsed);

        if (remainingCooldown > 0) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.warper.cooldown")
                            .append(" " + (remainingCooldown / 1000) + "s")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    private void sendMissingCyberwareMessage(ServerPlayer player) {
        OrganCap.OrganData data = OrganCap.getOrganData(player);
        if (data == null) return;

        if (!data.isTierAboveProsthetic(CSAugUtil.OrganSlots.BRAIN)) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.missing_brain")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }

        if (!data.isTierAboveProsthetic(CSAugUtil.OrganSlots.RIBS)) {
            player.displayClientMessage(
                    Component.translatable("message.csaugmentations.missing_ribs")
                            .withStyle(ChatFormatting.RED),
                    true
            );
        }
    }

    private void applyCooldown(Player player) {
        player.getPersistentData().putLong("WarperLastUsed", System.currentTimeMillis());
    }

    private void applyExhaustion(Player player) {
        player.causeFoodExhaustion(exhaustion);
    }

    private boolean isSafeLocation(Player player, Vec3 pos) {
        Level level = player.level();

        if (pos.y < level.getMinBuildHeight() || pos.y >= level.getMaxBuildHeight()) {
            return false;
        }

        BlockPos blockPos = BlockPos.containing(pos);
        BlockPos belowPos = blockPos.below();

        if (!level.getBlockState(belowPos).isSolid() && !level.getBlockState(belowPos).canBeReplaced()) {
            return false;
        }

        if (!level.getBlockState(blockPos).isAir() && !level.getBlockState(blockPos).canBeReplaced()) {
            return false;
        }
        if (!level.getBlockState(blockPos.above()).isAir() && !level.getBlockState(blockPos.above()).canBeReplaced()) {
            return false;
        }

        return level.noCollision(player, player.getBoundingBox().move(pos.subtract(player.position())));
    }

    private boolean hasRequiredCyberware(Player player) {
        OrganCap.OrganData data = OrganCap.getOrganData(player);
        if (data == null) return false;

        return data.isTierAboveProsthetic(CSAugUtil.OrganSlots.BRAIN) &&
                data.isTierAboveProsthetic(CSAugUtil.OrganSlots.RIBS);
    }
}
