package net.corespring.csaugmentations.Capability;

import net.corespring.csaugmentations.CSCommonConfigs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Cyberpsychosis {
    private static final Random RANDOM = new Random();
    private static final List<SoundEvent> PHANTOM_SOUNDS = Arrays.asList(
            SoundEvents.VILLAGER_WORK_LIBRARIAN,
            SoundEvents.CHEST_OPEN,
            SoundEvents.WOODEN_DOOR_OPEN,
            SoundEvents.GRAVEL_BREAK,
            SoundEvents.GRAVEL_PLACE,
            SoundEvents.STONE_BREAK,
            SoundEvents.STONE_PLACE,
            SoundEvents.GENERIC_EXTINGUISH_FIRE,
            SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR,
            SoundEvents.ZOMBIE_VILLAGER_AMBIENT,
            SoundEvents.ZOMBIE_AMBIENT,
            SoundEvents.CREEPER_PRIMED,
            SoundEvents.SKELETON_AMBIENT,
            SoundEvents.ENDERMAN_STARE,
            SoundEvents.ENDERMAN_AMBIENT,
            SoundEvents.GHAST_SCREAM
    );

    private static final List<Component> FAKE_ACTIONBAR_MESSAGES = Arrays.asList(
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_1"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_2"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_3"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_4"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_5"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_6"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_7"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_actionbar_8")
    );

    private static final List<Component> FAKE_PLAYER_MESSAGES = Arrays.asList(
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_1"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_2"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_3"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_4"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_5"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_6"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_7"),
            Component.translatable("cyberpsychosis.csaugmentations.fake_player_8")
    );

    private static final int SEVERITY_THRESHOLD = CSCommonConfigs.CYBERPSYCHOSIS_TRADING_THRESHOLD.get();
    private static final long MIN_INTERVAL = 1200;
    private static final long MAX_INTERVAL = 168000;
    private static final int MAX_SEVERITY = 100;
    private static final long COOLDOWN_DURATION = 100;
    private long nextSoundTime = -1;
    private long nextActionbarTime = -1;
    private long nextFakeChatTime = -1;
    private long nextPetMessageTime = -1;
    private long nextSeverityIncreaseTime = -1;
    private long lastRefusalTime = -1;
    private boolean hasSlept = false;
    private int severityLevel = 0;

    public boolean isOnCooldown() {
        return lastRefusalTime != -1 && System.currentTimeMillis() - lastRefusalTime < COOLDOWN_DURATION * 50;
    }

    public void startCooldown() {
        lastRefusalTime = System.currentTimeMillis();
    }

    public boolean shouldRefuseTrade() {
        int severityLevel = getSeverityLevel();
        if (severityLevel < SEVERITY_THRESHOLD) {
            return false;
        }

        if (isOnCooldown()) {
            return true;
        }

        double refusalChance = (double) (severityLevel - SEVERITY_THRESHOLD) / (MAX_SEVERITY - SEVERITY_THRESHOLD);
        if (RANDOM.nextDouble() < refusalChance) {
            startCooldown();
            return true;
        }
        return false;
    }

    public void handleCyberpsychosis(ServerPlayer player) {
        long gameTime = player.level().getGameTime();

        if (nextSeverityIncreaseTime == -1 || gameTime >= nextSeverityIncreaseTime) {
            if (severityLevel < MAX_SEVERITY) {
                severityLevel++;
                nextSeverityIncreaseTime = gameTime + 1200; //Ticks between Severity increases
            }
        }

        if (player.isSleeping()) {
            hasSlept = true;
        } else if (hasSlept) {
            if (player.level().isDay()) {
                reduceSeverityOnSleep();
            }
            hasSlept = false;
        }

        long soundInterval = calculateInterval(MIN_INTERVAL, MAX_INTERVAL, severityLevel);
        long actionbarInterval = calculateInterval(MIN_INTERVAL, MAX_INTERVAL, severityLevel);
        long fakeChatInterval = calculateInterval(MIN_INTERVAL, MAX_INTERVAL, severityLevel);
        long petMessageInterval = calculateInterval(MIN_INTERVAL, MAX_INTERVAL, severityLevel);

        if (nextSoundTime == -1 || gameTime >= nextSoundTime) {
            schizophrenia(player);
            nextSoundTime = gameTime + soundInterval;
        }

        if (nextActionbarTime == -1 || gameTime >= nextActionbarTime) {
            actionbarMessages(player);
            nextActionbarTime = gameTime + actionbarInterval;
        }

        if (nextFakeChatTime == -1 || gameTime >= nextFakeChatTime) {
            fakePlayerMessages(player);
            nextFakeChatTime = gameTime + fakeChatInterval;
        }

        if (nextPetMessageTime == -1 || gameTime >= nextPetMessageTime) {
            fakePetDeathMessages(player);
            nextPetMessageTime = gameTime + petMessageInterval;
        }
    }

    private long calculateInterval(long minInterval, long maxInterval, int severity) {
        double factor = (double) severity / MAX_SEVERITY;
        return minInterval + (long) ((maxInterval - minInterval) * (1 - factor));
    }

    private void schizophrenia(ServerPlayer player) {
        SoundEvent sound = PHANTOM_SOUNDS.get(RANDOM.nextInt(PHANTOM_SOUNDS.size()));
        Vec3 playerPos = player.position();
        Vec3 soundPos = playerPos.add(new Vec3(RANDOM.nextDouble() * 10 - 5, 0, RANDOM.nextDouble() * 10 - 5));

        player.level().playSound(null, soundPos.x, soundPos.y, soundPos.z, sound, SoundSource.AMBIENT, 1.0F, 1.0F);
    }

    private void actionbarMessages(ServerPlayer player) {
        Component message = FAKE_ACTIONBAR_MESSAGES.get(RANDOM.nextInt(FAKE_ACTIONBAR_MESSAGES.size()));
        player.displayClientMessage(message, true);
    }

    private void fakePlayerMessages(ServerPlayer player) {
        List<ServerPlayer> onlinePlayers = player.server.getPlayerList().getPlayers();
        if (!onlinePlayers.isEmpty()) {
            ServerPlayer fakeSender = onlinePlayers.get(RANDOM.nextInt(onlinePlayers.size()));
            if (fakeSender != player) {
                Component fakeMessage = FAKE_PLAYER_MESSAGES.get(RANDOM.nextInt(FAKE_PLAYER_MESSAGES.size()));
                Component formattedMessage = Component.translatable("chat.type.text", fakeSender.getDisplayName(), fakeMessage);

                player.sendSystemMessage(formattedMessage);
            }
        }
    }

    private void fakePetDeathMessages(ServerPlayer player) {
        List<Entity> pets = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(20));
        if (!pets.isEmpty()) {
            Entity pet = pets.get(RANDOM.nextInt(pets.size()));
            if (pet instanceof TamableAnimal tamablePet && tamablePet.isTame() && tamablePet.getOwner() == player) {
                List<Entity> nearbyEntities = player.level().getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(30));
                List<Entity> potentialCulprits = new ArrayList<>();

                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity livingEntity && entity != player && isHostile(livingEntity)) {
                        potentialCulprits.add(entity);
                    }
                }

                if (!potentialCulprits.isEmpty()) {
                    Entity culprit = potentialCulprits.get(RANDOM.nextInt(potentialCulprits.size()));
                    String culpritName = culprit instanceof Player ? culprit.getDisplayName().getString() : culprit.getName().getString();

                    Component message = Component.translatable("cyberpsychosis.csaugmentations.fake_pet_death_by_mob", pet.getName(), culpritName);
                    player.displayClientMessage(message, false);
                } else {
                    Component fallbackMessage = Component.translatable("cyberpsychosis.csaugmentations.fake_pet_death", pet.getName());
                    player.displayClientMessage(fallbackMessage, false);
                }
            }
        }
    }

    private boolean isHostile(LivingEntity entity) {
        return entity instanceof Monster ||
                (entity instanceof Wolf wolf && wolf.isAngry()) ||
                (entity instanceof EnderMan enderman && enderman.isCreepy());
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("nextSoundTime", nextSoundTime);
        tag.putLong("nextActionbarTime", nextActionbarTime);
        tag.putLong("nextFakeChatTime", nextFakeChatTime);
        tag.putLong("nextPetMessageTime", nextPetMessageTime);
        tag.putInt("severityLevel", severityLevel);
        tag.putLong("lastRefusalTime", lastRefusalTime);
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        nextSoundTime = nbt.getLong("nextSoundTime");
        nextActionbarTime = nbt.getLong("nextActionbarTime");
        nextFakeChatTime = nbt.getLong("nextFakeChatTime");
        nextPetMessageTime = nbt.getLong("nextPetMessageTime");
        severityLevel = nbt.getInt("severityLevel");
        lastRefusalTime = nbt.getLong("lastRefusalTime");
    }

    private void reduceSeverityOnSleep() {
        int reductionAmount = 10;
        reduceSeverity(reductionAmount);
    }


    public void reduceSeverity(int amount) {
        severityLevel = Math.max(0, severityLevel - amount);
    }

    public int getSeverityLevel() {
        return severityLevel;
    }
}
