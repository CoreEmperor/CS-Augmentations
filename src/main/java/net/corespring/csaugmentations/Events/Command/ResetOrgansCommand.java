package net.corespring.csaugmentations.Events.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ResetOrgansCommand {
    private static final SimpleCommandExceptionType ERROR_RESET_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.resetorgan.failed"));
    private static final String SUCCESS_MESSAGE_KEY = "commands.resetorgan.success";
    private static final int REQUIRED_PERMISSION_LEVEL = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(REQUIRED_PERMISSION_LEVEL))
                        .then(Commands.literal("resetOrgans")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ResetOrgansCommand::execute))
                        )
        );

        dispatcher.register(Commands.literal("csaugmentations").redirect(command));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");

        boolean success = resetOrgans(targetPlayer);

        if (!success) {
            throw ERROR_RESET_FAILED.create();
        }

        Component successMessage = Component.translatable(SUCCESS_MESSAGE_KEY, targetPlayer.getName().getString());
        source.sendSuccess(() -> successMessage, true);
        return 1;
    }

    private static boolean resetOrgans(ServerPlayer player) {
        OrganCap.getOrganData(player).pDefaultOrgans();
        return true;
    }
}
