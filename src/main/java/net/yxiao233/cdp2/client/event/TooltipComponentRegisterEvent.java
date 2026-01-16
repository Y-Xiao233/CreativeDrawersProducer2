package net.yxiao233.cdp2.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.renderer.tooltip.ItemTooltipComponent;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class TooltipComponentRegisterEvent {
    @SubscribeEvent
    public static void onRegistry(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(ItemTooltipComponent.class, ItemTooltipComponent::getClientTooltipComponent);
    }
}
