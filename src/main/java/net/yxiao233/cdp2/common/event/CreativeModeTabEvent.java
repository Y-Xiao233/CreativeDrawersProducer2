package net.yxiao233.cdp2.common.event;

import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPCrops;
import net.yxiao233.cdp2.common.registry.CDPTab;

public class CreativeModeTabEvent {
    public static void onBuild(BuildCreativeModeTabContentsEvent event){
        if(event.getTab() == CDPTab.CONTENT_TAB.asTab() || event.getTab() == CDPTab.MYSTICAL_AGRICULTURE_TAB.asTab()){
            event.accept(CDPCrops.PRUDENTIUM.getSeedsItem());
            event.accept(CDPCrops.TERTIUM.getSeedsItem());
            event.accept(CDPCrops.IMPERIUM.getSeedsItem());
            event.accept(CDPCrops.SUPREMIUM.getSeedsItem());
            event.accept(CDPCrops.INSANIUM.getSeedsItem());
        }
    }
}
