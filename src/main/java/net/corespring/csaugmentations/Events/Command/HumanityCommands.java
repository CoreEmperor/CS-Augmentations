package net.corespring.csaugmentations.Events.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Capability.OrganCap.OrganData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class HumanityCommands {
    private static final SimpleCommandExceptionType ERROR_GET_HUMANITY_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.gethumanity.failed"));
    private static final SimpleCommandExceptionType ERROR_SET_HUMANITY_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.sethumanity.failed"));

    private static final String GET_SUCCESS_MESSAGE_KEY = "commands.gethumanity.success";
    private static final String SET_SUCCESS_MESSAGE_KEY = "commands.sethumanity.success";

    private static final int REQUIRED_PERMISSION_LEVEL = 2;

    private static final int MIN_HUMANITY_VALUE = 0;
    private static final int MAX_HUMANITY_VALUE = 100;


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(REQUIRED_PERMISSION_LEVEL))
                        .then(Commands.literal("getHumanity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(HumanityCommands::getHumanity)
                                )
                        )
                        .then(Commands.literal("setHumanity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("value", IntegerArgumentType.integer(MIN_HUMANITY_VALUE, MAX_HUMANITY_VALUE))
                                                .executes(HumanityCommands::setHumanity)
                                        )
                                )
                        )
        );
    }

    private static int getHumanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");

        int humanityValue = getHumanityValue(targetPlayer);

        if (humanityValue == -1) {
            throw ERROR_GET_HUMANITY_FAILED.create();
        }

        Component successMessage = Component.translatable(GET_SUCCESS_MESSAGE_KEY, targetPlayer.getName().getString(), humanityValue);
        source.sendSuccess(() -> successMessage, true);

        return 1;
    }

    private static int setHumanity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");
        int newValue = IntegerArgumentType.getInteger(context, "value");

        boolean success = setHumanityValue(targetPlayer, newValue);

        if (!success) {
            throw ERROR_SET_HUMANITY_FAILED.create();
        }

        Component successMessage = Component.translatable(SET_SUCCESS_MESSAGE_KEY, targetPlayer.getName().getString(), newValue);
        source.sendSuccess(() -> successMessage, true);

        return 1;
    }

    private static int getHumanityValue(ServerPlayer player) {
        OrganData organData = OrganCap.getOrganData(player);
        if (organData != null) {
            return organData.getHumanityLimit();
        }
        return -1;
    }

    private static boolean setHumanityValue(ServerPlayer player, int newValue) {
        OrganData organData = OrganCap.getOrganData(player);
        if (organData != null) {
            organData.setHumanityLimit(newValue);
            organData.updatePersistentData();
            return true;
        }
        return false;
    }
}