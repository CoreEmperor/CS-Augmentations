package net.corespring.csaugmentations.Capability;

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

    private static final long MIN_INTERVAL = 5 * 20;
    private static final long MAX_INTERVAL = 20 * 20;

    private long nextSoundTime = -1;
    private long nextActionbarTime = -1;
    private long nextFakeChatTime = -1;
    private long nextPetMessageTime = -1;

    public void handleCyberpsychosis(ServerPlayer player) {
        long gameTime = player.level().getGameTime();

        if (nextSoundTime == -1 || gameTime >= nextSoundTime) {
            schizophrenia(player);
            nextSoundTime = gameTime + RANDOM.nextLong(MIN_INTERVAL, MAX_INTERVAL + 1);
        }

        if (nextActionbarTime == -1 || gameTime >= nextActionbarTime) {
            actionbarMessages(player);
            nextActionbarTime = gameTime + RANDOM.nextLong(MIN_INTERVAL, MAX_INTERVAL + 1);
        }

        if (nextFakeChatTime == -1 || gameTime >= nextFakeChatTime) {
            fakePlayerMessages(player);
            nextFakeChatTime = gameTime + RANDOM.nextLong(MIN_INTERVAL, MAX_INTERVAL + 1);
        }

        if (nextPetMessageTime == -1 || gameTime >= nextPetMessageTime) {
            fakePetDeathMessages(player);
            nextPetMessageTime = gameTime + RANDOM.nextLong(MIN_INTERVAL, MAX_INTERVAL + 1);
        }
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
}