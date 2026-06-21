package net.yxiao233.cdp2.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeDrawersProducerCommand {
    @SubscribeEvent
    public static void registry(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("cdp2")
                .then(Commands.literal("doAuraShow")
                        .executes(context -> {
                            if(!CreativeDrawersProducer2.hideNeededAura){
                                context.getSource().sendSuccess(() -> Component.translatable("command.cdp2.hide_needed_aura"),false);
                            }else{
                                context.getSource().sendSuccess(() -> Component.translatable("command.cdp2.show_needed_aura"),false);
                            }
                            CreativeDrawersProducer2.hideNeededAura = !CreativeDrawersProducer2.hideNeededAura;
                            return 1;
                        })
                )
        );
    }
}
