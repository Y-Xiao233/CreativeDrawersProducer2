package net.yxiao233.cdp2.common.registry;

import com.blakebr0.mysticalagriculture.item.EssenceItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;
import net.yxiao233.cdp2.common.item.CreativeShardItem;
import net.yxiao233.cdp2.common.item.CreativeShardTier;
import net.yxiao233.cdp2.common.item.RandomCreativeDrawerItem;
import net.yxiao233.cdp2.integration.mystical_agriculture.CDPCropTier;

import java.util.Arrays;
import java.util.HashMap;

public class CDPItem{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CreativeDrawersProducer2.MODID);
    public static final CDPItemDeferredRegister RANDOM_CREATIVE_DRAWER = CDPItemDeferredRegister.registryItem("random_creative_drawer", () -> new RandomCreativeDrawerItem(new Item.Properties())).addToTab(CDPTab.DRAWER_TAB);
    public static final HashMap<ResourceLocation, CDPItemDeferredRegister> SHARDS;
    public static final CDPItemDeferredRegister ABSOLUTE_ESSENCE = CDPItemDeferredRegister.registryItem("absolute_essence",() -> new EssenceItem(CDPCropTier.SEVEN)).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPItemDeferredRegister SUPREME_ESSENCE = CDPItemDeferredRegister.registryItem("supreme_essence",() -> new EssenceItem(CDPCropTier.EIGHT)).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPItemDeferredRegister COSMIC_ESSENCE = CDPItemDeferredRegister.registryItem("cosmic_essence",() -> new EssenceItem(CDPCropTier.NINE)).addToTab(CDPTab.CONTENT_TAB);
    public static final CDPItemDeferredRegister INFINITE_ESSENCE = CDPItemDeferredRegister.registryItem("infinite_essence",() -> new EssenceItem(CDPCropTier.TEN)).addToTab(CDPTab.CONTENT_TAB);
    public static void init(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
    static {
        SHARDS = new HashMap<>();
        Arrays.stream(CreativeShardTier.values()).toList().forEach(tier ->{
            SHARDS.put(tier.getId(),CDPItemDeferredRegister.registryItem(tier.getName(),() -> new CreativeShardItem(new Item.Properties(),tier.getTier())).addToTab(CDPTab.CONTENT_TAB));
        });
    }
}
