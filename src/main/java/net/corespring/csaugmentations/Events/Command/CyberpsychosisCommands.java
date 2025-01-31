package net.corespring.csaugmentations.Events.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CyberpsychosisCommands {
    private static final SimpleCommandExceptionType ERROR_GET_SEVERITY_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.getseverity.failed"));
    private static final SimpleCommandExceptionType ERROR_SET_SEVERITY_FAILED =
            new SimpleCommandExceptionType(Component.translatable("commands.setseverity.failed"));

    private static final String GET_SUCCESS_KEY = "commands.getseverity.success";
    private static final String SET_SUCCESS_KEY = "commands.setseverity.success";

    private static final int REQUIRED_PERMISSION = 2;

    private static final int MIN_SEVERITY = 0;
    private static final int MAX_SEVERITY = 100;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(REQUIRED_PERMISSION))
                        .then(Commands.literal("getSeverity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(CyberpsychosisCommands::getSeverity)
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("csaugmentations")
                        .requires(source -> source.hasPermission(REQUIRED_PERMISSION))
                        .then(Commands.literal("setSeverity")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("value", IntegerArgumentType.integer(MIN_SEVERITY, MAX_SEVERITY))
                                                .executes(CyberpsychosisCommands::setSeverity)
                                        )
                                )
                        )
        );
    }

    private static int getSeverity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        OrganCap.OrganData data = OrganCap.getOrganData(target);

        if(data == null || data.getCyberpsychosis() == null) {
            throw ERROR_GET_SEVERITY_FAILED.create();
        }

        int severity = data.getCyberpsychosis().getSeverityLevel();
        Component message = Component.translatable(GET_SUCCESS_KEY, target.getName().getString(), severity);
        context.getSource().sendSuccess(() -> message, true);
        return severity;
    }

    private static int setSeverity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(context, "target");
        int newValue = IntegerArgumentType.getInteger(context, "value");
        OrganCap.OrganData data = OrganCap.getOrganData(target);

        if(data == null || data.getCyberpsychosis() == null) {
            throw ERROR_SET_SEVERITY_FAILED.create();
        }

        data.getCyberpsychosis().reduceSeverity(data.getCyberpsychosis().getSeverityLevel());
        data.getCyberpsychosis().reduceSeverity(-newValue);

        data.updatePersistentData();

        Component message = Component.translatable(SET_SUCCESS_KEY,
                target.getName().getString(),
                newValue);

        context.getSource().sendSuccess(() -> message, true);
        return 1;
    }
}