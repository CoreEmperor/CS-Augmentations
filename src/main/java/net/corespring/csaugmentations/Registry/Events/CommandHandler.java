package net.corespring.csaugmentations.Registry.Events;

import com.mojang.brigadier.CommandDispatcher;
import net.corespring.csaugmentations.CSAugmentations;
import net.corespring.csaugmentations.Registry.Utility.Command.GetHumanityCommand;
import net.corespring.csaugmentations.Registry.Utility.Command.ResetOrgansCommand;
import net.corespring.csaugmentations.Registry.Utility.Command.SetHumanityCommand;
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
        SetHumanityCommand.register(dispatcher);
        GetHumanityCommand.register(dispatcher);
    }
}
