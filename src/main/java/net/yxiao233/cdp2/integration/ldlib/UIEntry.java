package net.yxiao233.cdp2.integration.ldlib;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.mojang.datafixers.types.Func;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.util.LDLibUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UIEntry {
    private final BlockUIMenuType.BlockUIHolder holder;
    private final List<UIElement> elements = new ArrayList<>();
    private final UIElement root;
    private UIEntry(BlockUIMenuType.BlockUIHolder holder, UIElement root){
        this.holder = holder;
        this.root = root;
    }

    public static UIEntry create(BlockUIMenuType.BlockUIHolder holder){
        return create(holder,LDLibUtil.createBaseRoot());
    }

    public static UIEntry create(BlockUIMenuType.BlockUIHolder holder, UIElement root){
        return new UIEntry(holder,root);
    }

    public HoverTooltips itemHoverTip(Item item){
        return LDLibUtil.itemHoverTip(item);
    }

    public UIEntry fakeItemElement(Item item, float posX, float posY){
        Objects.requireNonNull(item);
        elements.add(LDLibUtil.fakeItemElement(item, posX, posY));
        return this;
    }

    public UIEntry text(Component text){
        elements.add(LDLibUtil.text(text));
        return this;
    }

    public UIEntry itemSlot(ItemStackHandler handler, int index){
        elements.add(new ItemSlot().bind(handler,index));
        return this;
    }

    public UIEntry itemSlot(BlockCapabilityMap capabilityMap){
        return itemSlot(capabilityMap,0);
    }

    public UIEntry itemSlot(BlockCapabilityMap capabilityMap, int index){
        return itemSlot((ItemStackHandler) capabilityMap.getItemHandler(),index);
    }

    public UIEntry playerInventory(){
        elements.add(new InventorySlots());
        return this;
    }
    public UIEntry operation(UIElement... elements){
        this.elements.addAll(Arrays.stream(elements).toList());
        return this;
    }

    public UIEntry operation(Supplier<List<UIElement>> consumer){
        this.elements.addAll(consumer.get());
        return this;
    }

    public UIEntry operation(Consumer<List<UIElement>> consumer){
        consumer.accept(this.elements);
        return this;
    }


    public ModularUI build(){
        this.root.addChildren(this.elements.toArray(UIElement[]::new));
        return ModularUI.of(UI.of(this.root),this.holder.player);
    }
}
