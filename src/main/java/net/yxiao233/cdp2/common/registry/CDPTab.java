package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.registry.CDPCreativeModeTabDeferredRegister;

public class CDPTab {
    public static final  DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreativeDrawersProducer2.MODID);
    public static final CDPCreativeModeTabDeferredRegister DRAWER_TAB = CDPCreativeModeTabDeferredRegister.registrySimpleTab("drawer",() -> CDPBlock.DIAMOND_CREATIVE_DRAWER.getBlockDeferredHolder().getItemRegister());
    public static final CDPCreativeModeTabDeferredRegister MACHINE_TAB = CDPCreativeModeTabDeferredRegister.registrySimpleTab("machine", () -> CDPBlock.UPGRADE_STATION.getBlockDeferredHolder().getItemRegister());
    public static final CDPCreativeModeTabDeferredRegister CONTENT_TAB = CDPCreativeModeTabDeferredRegister.registrySimpleTab("content", () -> CDPItem.SHARDS.values().stream().toList().getFirst());
    public static void init(IEventBus eventBus){
        TABS.register(eventBus);
    }
}
