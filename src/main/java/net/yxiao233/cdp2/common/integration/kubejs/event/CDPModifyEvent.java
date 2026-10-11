package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CDPModifyEvent {
    public static final EventGroup GROUP = EventGroup.of("CDPModifyEvents");
    public static final EventHandler BLOCK = GROUP.startup("block", () -> BlockModifyEvent.class);
    /** Replaces the items Cataclysm needs to activate its bosses / open the door of seal, server side (reloadable) */
    public static final EventHandler CATACLYSM_ITEM = GROUP.server("cataclysmItem", () -> CataclysmItemModifyEvent.class);
}
