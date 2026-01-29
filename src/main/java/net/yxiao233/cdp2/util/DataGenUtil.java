package net.yxiao233.cdp2.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;

import java.util.HashMap;
import java.util.function.IntFunction;

public class DataGenUtil {
    public static Item[] blockEntityMapForItems(HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<?>> map){
        return map.values().stream().map(CDPBlockEntityDeferredRegister::asItem).toArray(Item[]::new);
    }

    public static Block[] blockEntityMapForBlocks(HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<?>> map){
        return map.values().stream().map(CDPBlockEntityDeferredRegister::asBlock).toArray(Block[]::new);
    }

    public static Item[] itemMapForItems(HashMap<ResourceLocation, CDPItemDeferredRegister> map){
        return map.values().stream().map(CDPItemDeferredRegister::asItem).toArray(Item[]::new);
    }

    public static <T extends BlockEntity> Item[] typedBlockEntityMapForItems(HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<T>> map){
        return map.values().stream().map(CDPBlockEntityDeferredRegister::asItem).toArray(Item[]::new);
    }

    public static <T extends BlockEntity> Block[] typedBlockEntityMapForBlocks(HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<T>> map){
        return map.values().stream().map(CDPBlockEntityDeferredRegister::asBlock).toArray(Block[]::new);
    }
}
