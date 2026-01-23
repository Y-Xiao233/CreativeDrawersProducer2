package net.yxiao233.cdp2.util;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.TabView;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import com.lowdragmc.lowdraglib2.gui.ui.utils.ModularUITooltipComponent;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.client.gui.CDPSprites;
import net.yxiao233.cdp2.misc.UpgradePointManager;
import org.appliedenergistics.yoga.YogaEdge;
import org.appliedenergistics.yoga.YogaFlexDirection;
import org.appliedenergistics.yoga.YogaGutter;
import org.appliedenergistics.yoga.YogaJustify;

import java.util.Objects;

public class LDLibUtil {
    public static HoverTooltips itemHoverTip(Item item){
        String translateKey = item.getDescriptionId();
        String id = BuiltInRegistries.ITEM.getKey(item).toString();
        String rawModId = Objects.requireNonNull(item.getCreatorModId(item.getDefaultInstance()));
        char firstChar = rawModId.charAt(0);
        String modId = String.valueOf(firstChar).toUpperCase() + rawModId.substring(1);
        if(rawModId.equals(CreativeDrawersProducer2.MODID)){
            modId = "Creative Drawers Producer 2";
        }
        return HoverTooltips.empty()
                .append(Component.translatable(translateKey))
                .append(Component.literal(id).withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(modId).withStyle(ChatFormatting.BLUE,ChatFormatting.ITALIC));
    }

    public static HoverTooltips itemHoverTipWithTexture(Item item){
        String translateKey = item.getDescriptionId();
        String id = BuiltInRegistries.ITEM.getKey(item).toString();
        String rawModId = Objects.requireNonNull(item.getCreatorModId(item.getDefaultInstance()));
        char firstChar = rawModId.charAt(0);
        String modId = String.valueOf(firstChar).toUpperCase() + rawModId.substring(1);
        if(rawModId.equals(CreativeDrawersProducer2.MODID)){
            modId = "Creative Drawers Producer 2";
        }
        return HoverTooltips.empty()
                .append(Component.translatable(translateKey))
                .tooltipComponent(new ModularUITooltipComponent(new UIElement().layout(layoutStyle -> layoutStyle
                        .width(16)
                        .height(16)
                ).style(basicStyle -> {
                    basicStyle.background(new ItemStackTexture(item));
                })))
                .append(Component.literal(id).withStyle(ChatFormatting.DARK_GRAY))
                .append(Component.literal(modId).withStyle(ChatFormatting.BLUE,ChatFormatting.ITALIC));
    }

    public static void clickOpenJei(RecipeIngredientRole role, UIElement element, ItemStack stack){
        element.addEventListener(UIEvents.CLICK,event -> {
           JeiUtil.openRecipeGui(role,stack);
        });
    }

    public static UIElement fakeItemElement(Item item, float posX, float posY){
        Objects.requireNonNull(item);
        return new UIElement().layout(layoutStyle -> layoutStyle
                .width(16)
                .height(16)
                .setPosition(YogaEdge.LEFT,posX)
                .setPosition(YogaEdge.TOP,posY)
        ).style(basicStyle -> {
            basicStyle.background(new ItemStackTexture(item));
        }).addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
            event.hoverTooltips = itemHoverTip(item);
        }).addEventListener(UIEvents.CLICK, event -> {
            JeiUtil.openRecipeGui(RecipeIngredientRole.INPUT,item.getDefaultInstance());
        });
    }

    public static UIElement fakeItemElement(Item item){
        Objects.requireNonNull(item);
        return new UIElement().layout(layoutStyle -> layoutStyle
                .width(16)
                .height(16)
        ).style(basicStyle -> {
            basicStyle.background(new ItemStackTexture(item));
        }).addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
            event.hoverTooltips = itemHoverTip(item);
        }).addEventListener(UIEvents.CLICK, event -> {
            JeiUtil.openRecipeGui(RecipeIngredientRole.INPUT,item.getDefaultInstance());
        });
    }

    public static Label text(Component text){
        return (Label) new Label().setText(text);
    }

    public static UIElement inputSlot(ItemStackHandler handler, int index){
        return itemSlot(SlotRole.INPUT,handler,index);
    }

    public static UIElement inputSlot(BlockCapabilityMap capabilityMap){
        return inputSlot(capabilityMap,0);
    }

    public static UIElement inputSlot(BlockCapabilityMap capabilityMap, int index){
        return itemSlot(SlotRole.INPUT, capabilityMap.getItemHandler(),index);
    }

    public static UIElement itemSlot(SlotRole role, ItemStackHandler handler, int index){
        ItemSlot slot = new ItemSlot().bind(handler,index);
        switch (role){
            case INPUT -> slot.style(basicStyle -> basicStyle.background(CDPSprites.INPUT_SLOT));
            case OUTPUT -> slot.style(basicStyle -> basicStyle.background(CDPSprites.OUTPUT_SLOT));
            default -> slot.style(basicStyle -> basicStyle.background(CDPSprites.CATALYST_SLOT));
        }
        return slot;
    }

    public static UIElement playerInventory(){
        return new InventorySlots();
    }

    public static UIElement createBaseRoot(){
        return new UIElement().layout(layoutStyle -> layoutStyle
                .setPadding(YogaEdge.ALL, 4)
                .setGap(YogaGutter.ALL, 2)
                .setJustifyContent(YogaJustify.CENTER)
        );
    }

    public static TabView createTabWithBackground(IGuiTexture background){
        return (TabView) new TabView().layout(layoutStyle -> layoutStyle
                .setPadding(YogaEdge.ALL, 4)
                .setGap(YogaGutter.ALL, 2)
                .setJustifyContent(YogaJustify.CENTER)
        ).style(basicStyle -> {
            basicStyle.background(background);
        });
    }

    public static TabView createBaseTab(){
        return (TabView) new TabView().layout(layoutStyle -> layoutStyle
                .setPadding(YogaEdge.ALL, 4)
                .setGap(YogaGutter.ALL, 2)
                .setJustifyContent(YogaJustify.CENTER)
        );
    }

    public static TabView createTabWithBackground(){
        return createTabWithBackground(Sprites.BORDER);
    }

    public static UIElement createBaseRootWithBackground(IGuiTexture background){
        return new UIElement().layout(layoutStyle -> layoutStyle
                .setPadding(YogaEdge.ALL, 4)
                .setGap(YogaGutter.ALL, 2)
                .setJustifyContent(YogaJustify.CENTER)
        ).style(basicStyle -> {
            basicStyle.background(background);
        });
    }

    public static UIElement createBaseRootWithBackground(){
        return createBaseRootWithBackground(Sprites.BORDER);
    }

    public static UIElement flex(float flex){
        return new UIElement().layout(layoutStyle -> layoutStyle.flex(flex));
    }

    public static UIElement widthFlex(float flex){
        return new UIElement().layout(layoutStyle -> layoutStyle.width(flex));
    }
    public static UIElement rowElement(){
        return new UIElement().layout(layoutStyle -> layoutStyle
                .flexDirection(YogaFlexDirection.ROW)
        );
    }

    public static UIElement newOperation(UIElement... elements){
        return operation(createBaseRoot(),elements);
    }

    public static UIElement operation(UIElement root, UIElement... elements){
        return root.addChildren(elements);
    }

    public static ModularUI createSimple(BlockUIMenuType.BlockUIHolder holder, UIElement... elements){
        return ModularUI.of(UI.of(newOperation(elements)),holder.player);
    }

    public static ModularUI createSimple(BlockUIMenuType.BlockUIHolder holder, UIElement root, UIElement... elements){
        return ModularUI.of(UI.of(root),holder.player);
    }

    public enum SlotRole{
        INPUT,
        OUTPUT,
        CATALYST;
    }

    public interface UpgradeScreenCallBack{
        void open(BlockUIMenuType.BlockUIHolder holder, UpgradePointManager manger, int totalPoint);
    }
}
