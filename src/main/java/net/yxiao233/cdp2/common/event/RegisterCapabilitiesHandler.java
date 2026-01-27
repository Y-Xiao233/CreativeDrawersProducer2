package net.yxiao233.cdp2.common.event;

import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.common.registry.CDPBlock;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterCapabilitiesHandler {

    @SubscribeEvent
    public static void onRegister(RegisterCapabilitiesEvent event){
        BlockCapabilityMap.registryAll(event);

        CDPBlock.POTS_MAP.values().forEach(pot ->{
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, pot.asBlockEntityType(), (blockEntity, side) -> {
                return side == Direction.DOWN ? new SidedInvWrapper(blockEntity, Direction.DOWN) : null;
            });
        });
    }
}
