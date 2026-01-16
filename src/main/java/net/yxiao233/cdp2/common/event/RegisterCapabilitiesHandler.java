package net.yxiao233.cdp2.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterCapabilitiesHandler {

    @SubscribeEvent
    public static void onRegister(RegisterCapabilitiesEvent event){
        BlockCapabilityMap.registryAll(event);
    }
}
