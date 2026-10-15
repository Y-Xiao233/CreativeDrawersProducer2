package net.yxiao233.cdp2.common.event;

import dev.latvian.mods.kubejs.script.ScriptType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPModifyEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.CataclysmItemModifyEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CDPServerConfigEvent {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event){
        reload();
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event){
        if(event.getPlayer() == null){
            reload();
        }
    }

    private static void reload(){
        CDPCataclysmItems.reset();
        try{
            CDPModifyEvent.CATACLYSM_ITEM.post(ScriptType.SERVER,new CataclysmItemModifyEvent());
        }catch(Exception exception){
            CreativeDrawersProducer2.LOGGER.error("Failed to read the kubejs cataclysm item configuration",exception);
        }
    }
}
