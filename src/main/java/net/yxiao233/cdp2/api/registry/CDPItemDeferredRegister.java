package net.yxiao233.cdp2.api.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class CDPItemDeferredRegister {
    private final DeferredHolder<Item,Item> itemDeferredHolder;
    private static final Map<ResourceLocation, List<CDPItemDeferredRegister>> map = new ConcurrentHashMap<>();
    private CDPItemDeferredRegister(DeferredHolder<Item,Item> itemDeferredHolder){
        this.itemDeferredHolder = itemDeferredHolder;
    }
    public static CDPItemDeferredRegister registrySimpleItem(String name, @Nullable Item.Properties properties){
        return registryItem(name, () -> new Item(properties == null ? new Item.Properties() : properties));
    }

    public static CDPItemDeferredRegister registryItem(String name, @Nonnull Supplier<? extends Item> supplier){
        DeferredHolder<Item,Item> itemHolder = CDPItem.ITEMS.register(name,supplier);
        return new CDPItemDeferredRegister(itemHolder);
    }

    public static CDPItemDeferredRegister registrySimpleItem(String name){
        return registrySimpleItem(name,null);
    }

    public CDPItemDeferredRegister addToTab(@Nonnull CDPCreativeModeTabDeferredRegister tab){
        if(tab.isAddItemsWhileRegistry()){
            map.computeIfAbsent(tab.getTab().getId(), k -> new CopyOnWriteArrayList<>()).add(this);
        }
        return this;
    }

    public static void addToTab(@Nonnull String name, CreativeModeTab.Output output){
        if(map.isEmpty()){
            return;
        }

        map.forEach((location, registers) -> {
            if(location.equals(CreativeDrawersProducer2.makeId(name))){
                registers.forEach(register -> {
                    output.accept(register.asStack());
                });
            }
        });
    }

    public DeferredHolder<Item,Item> getItemHolder(){
        return itemDeferredHolder;
    }

    public Item asItem(){
        return itemDeferredHolder.get();
    }

    public ItemStack asStack(){
        return asItem().getDefaultInstance();
    }
}
