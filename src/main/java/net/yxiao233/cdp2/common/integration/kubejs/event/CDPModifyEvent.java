package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CDPModifyEvent {
    public static final EventGroup GROUP = EventGroup.of("CDPModifyEvents");
    public static final EventHandler BLOCK = GROUP.startup("block", () -> BlockModifyEvent.class);
}
