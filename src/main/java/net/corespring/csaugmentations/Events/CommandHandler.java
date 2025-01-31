package net.corespring.csaugmentations.Events;

import com.mojang.brigadier.CommandDispatcher;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Events.Command.CyberpsychosisCommands;
import net.corespring.csaugmentations.Events.Command.HumanityCommands;
import net.corespring.csaugmentations.Events.Command.ResetOrgansCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CSAugmentations.MOD_ID)
public class CommandHandler {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        ResetOrgansCommand.register(dispatcher);
        HumanityCommands.register(dispatcher);
        CyberpsychosisCommands.register(dispatcher);
    }
}
