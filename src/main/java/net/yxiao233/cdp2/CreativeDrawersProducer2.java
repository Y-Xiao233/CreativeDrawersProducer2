package net.yxiao233.cdp2;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPTab;
import org.slf4j.Logger;

@Mod(CreativeDrawersProducer2.MODID)
public class CreativeDrawersProducer2 {
    public static final String MODID = "cdp2";
    public static final Logger LOGGER = LogUtils.getLogger();
    public CreativeDrawersProducer2(IEventBus modEventBus, ModContainer modContainer) {
        CDPItem.ITEMS.register(modEventBus);
        CDPBlock.BLOCKS.register(modEventBus);
        CDPBlock.BLOCK_ENTITIES.register(modEventBus);
        CDPTab.TABS.register(modEventBus);
    }

    public static ResourceLocation makeId(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
