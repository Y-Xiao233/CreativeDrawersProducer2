package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.yxiao233.cdp2.common.integration.kubejs.util.KubeUtils;
import net.yxiao233.cdp2.common.registry.CDPBlock;

public class BlockRegistryEvent implements KubeEvent {
    public static void createCreativeDrawer(String name, Object infinityItem){
        CDPBlock.registerCreativeDrawer(name, KubeUtils.getItemStackFromObject(infinityItem));
    }
}
