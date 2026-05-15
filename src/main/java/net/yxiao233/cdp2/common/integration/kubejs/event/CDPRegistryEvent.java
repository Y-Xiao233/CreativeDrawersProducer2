package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitualDefinition;

public class CDPRegistryEvent {
    public static final EventGroup GROUP = EventGroup.of("CDPStartupEvents");
    public static final EventHandler REGISTRY = GROUP.startup("registry", () -> RegistryDefinitionEvent.class);
}
