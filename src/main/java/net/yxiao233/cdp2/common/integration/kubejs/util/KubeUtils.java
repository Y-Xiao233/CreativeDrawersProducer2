package net.yxiao233.cdp2.common.integration.kubejs.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public class KubeUtils {
    public static Supplier<Item> getItemFromObject(Object obj){
        return switch (obj){
            case Item item -> () -> item;
            case ResourceLocation location -> getItemFromId(location);
            case String s -> getItemFromString(s);
            case ItemStack stack -> stack::getItem;
            case ItemLike itemLike -> itemLike::asItem;
            case null -> () -> Items.AIR;
            default -> throw new UnsupportedOperationException("Type Unknown");
        };
    }

    public static Supplier<Item> getItemFromString(String s){
        return getItemFromId(getIdFromString(s));
    }

    public static Supplier<Item> getItemFromId(ResourceLocation location){
        if(BuiltInRegistries.ITEM.containsKey(location)){
            return () -> BuiltInRegistries.ITEM.get(location);
        }
        return () -> Items.AIR;
    }

    public static ResourceLocation getIdFromString(String s){
        String[] split = s.split(":");
        return ResourceLocation.fromNamespaceAndPath(split[0],split[1]);
    }
}
