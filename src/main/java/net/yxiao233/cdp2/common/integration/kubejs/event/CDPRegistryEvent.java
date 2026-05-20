package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public class CDPRegistryEvent {
    public static final EventGroup GROUP = EventGroup.of("CDPRegistryEvents");
    public static final EventHandler ITEM = GROUP.startup("item", () -> ItemRegistryEvent.class);
    public static final EventHandler BLOCK = GROUP.startup("block", () -> BlockRegistryEvent.class);
}
