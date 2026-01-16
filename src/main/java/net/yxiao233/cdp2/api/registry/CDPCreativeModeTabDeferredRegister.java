package net.yxiao233.cdp2.api.registry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.common.registry.CDPTab;

import java.util.List;
import java.util.function.Supplier;

public class CDPCreativeModeTabDeferredRegister {
    private final DeferredHolder<CreativeModeTab,CreativeModeTab> creativeTabHolder;
    private boolean addItemsWhileRegistry = true;
    private CDPCreativeModeTabDeferredRegister(DeferredHolder<CreativeModeTab,CreativeModeTab> creativeTabHolder){
        this.creativeTabHolder = creativeTabHolder;
    }

    public CDPCreativeModeTabDeferredRegister canAddItemsWhileRegistry(boolean canAddItemsWhileRegistry){
        this.addItemsWhileRegistry = canAddItemsWhileRegistry;
        return this;
    }

    public boolean isAddItemsWhileRegistry() {
        return addItemsWhileRegistry;
    }

    public static CDPCreativeModeTabDeferredRegister registrySimpleTab(String name, ItemStack icon){
        return registryTab(name,() -> CreativeModeTab.builder()
                .icon(() -> icon)
                .title(Component.translatable("itemGroup.cdp2." + name))
                .displayItems((itemDisplayParameters, output) -> {
                    CDPItemDeferredRegister.addToTab(name,output);
                })
                .build()
        );
    }

    public static CDPCreativeModeTabDeferredRegister registrySimpleTab(String name, Supplier<CDPItemDeferredRegister> icon){
        return registryTab(name,() -> CreativeModeTab.builder()
                .icon(() -> icon.get().asStack())
                .title(Component.translatable("itemGroup.cdp2." + name))
                .displayItems((itemDisplayParameters, output) -> {
                    CDPItemDeferredRegister.addToTab(name,output);
                })
                .build()
        );
    }

    public static CDPCreativeModeTabDeferredRegister registrySimpleTab(String name, Supplier<ItemStack> icon, List<ItemLike> displayItems){
        return registryTab(name,() -> CreativeModeTab.builder()
                .icon(icon)
                .title(Component.translatable("itemGroup.cdp2." + name))
                .displayItems((itemDisplayParameters, output) -> {
                    CDPItemDeferredRegister.addToTab(name,output);
                    displayItems.forEach(output::accept);
                })
                .build()
        );
    }

    public static CDPCreativeModeTabDeferredRegister registryTab(String name, Supplier<? extends CreativeModeTab> supplier){
        DeferredHolder<CreativeModeTab,CreativeModeTab> creativeTabHolder = CDPTab.TABS.register(name,supplier);
        return new CDPCreativeModeTabDeferredRegister(creativeTabHolder);
    }

    public DeferredHolder<CreativeModeTab, CreativeModeTab> getTab(){
        return creativeTabHolder;
    }

    public CreativeModeTab asTab(){
        return creativeTabHolder.get();
    }
}
