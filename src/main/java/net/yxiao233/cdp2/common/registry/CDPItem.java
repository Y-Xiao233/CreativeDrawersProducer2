package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;
import net.yxiao233.cdp2.common.item.RandomCreativeDrawerItem;

public class CDPItem{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CreativeDrawersProducer2.MODID);
    public static final CDPItemDeferredRegister RANDOM_CREATIVE_DRAWER = CDPItemDeferredRegister.registryItem("random_creative_drawer", () -> new RandomCreativeDrawerItem(new Item.Properties())).addToTab(CDPTab.DRAWER_TAB);
}
