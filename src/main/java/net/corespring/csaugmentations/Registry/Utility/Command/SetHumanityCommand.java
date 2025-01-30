package net.corespring.csaugmentations.Registry.Utility.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.corespring.csaugmentations.Capability.OrganCap.OrganData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetHumanityCommand {
    private static final SimpleCommandExceptionType ERROR_CHANGE_HUMANITY_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.sethumanity.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("setHumanity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("value", IntegerArgumentType.integer(0, 100))
                                                .executes(SetHumanityCommand::execute))
                                )
                        )
        );

        dispatcher.register(Commands.literal("csaugmentations").redirect(command));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");
        int newValue = IntegerArgumentType.getInteger(context, "value");

        boolean success = changeHumanity(targetPlayer, newValue);

        if (!success) {
            throw ERROR_CHANGE_HUMANITY_FAILED.create();
        }

        Component successMessage = Component.translatable("commands.sethumanity.success", targetPlayer.getName().getString(), newValue);
        source.sendSuccess(() -> successMessage, true);
        return 1;
    }

    private static boolean changeHumanity(ServerPlayer player, int newValue) {
        OrganData organData = OrganCap.getOrganData(player);
        if (organData != null) {
            organData.setHumanityLimit(newValue);
            organData.updatePersistentData();
            return true;
        }
        return false;
    }
}
