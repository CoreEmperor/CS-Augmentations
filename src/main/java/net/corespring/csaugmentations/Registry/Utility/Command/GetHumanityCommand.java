package net.corespring.csaugmentations.Registry.Utility.Command;

import com.mojang.brigadier.CommandDispatcher;
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

public class GetHumanityCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("getHumanity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(GetHumanityCommand::execute))
                        )
        );

        dispatcher.register(Commands.literal("csaugmentations").redirect(command));
    }

    private static int execute(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, "target");

        int humanityValue = getHumanity(targetPlayer);

        Component successMessage = Component.translatable("commands.gethumanity.success", targetPlayer.getName().getString(), humanityValue);
        source.sendSuccess(() -> successMessage, true);
        return 1;
    }

    private static int getHumanity(ServerPlayer player) throws CommandSyntaxException {
        OrganData organData = OrganCap.getOrganData(player);
        if (organData != null) {
            return organData.getHumanityLimit();
        }
        throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.translatable("commands.gethumanity.failed")), Component.translatable("commands.gethumanity.failed"));
    }
}
