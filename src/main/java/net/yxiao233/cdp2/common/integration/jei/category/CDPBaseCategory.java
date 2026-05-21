package net.yxiao233.cdp2.common.integration.jei.category;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.yxiao233.cdp2.client.gui.AllGuiTextures;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class CDPBaseCategory<T extends Recipe<?>> implements IRecipeCategory<T> {
    public final RecipeType<T> type;
    public Component title;
    public final IDrawable background;
    public final IDrawable icon;

    public CDPBaseCategory(IGuiHelper helper, RecipeType<T> type, Component title, Item icon, int width, int height) {
        this.type = type;
        this.title = title;
        this.background = helper.createBlankDrawable(width,height);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,new ItemStack(icon));
    }
    @Override
    public @NotNull RecipeType<T> getRecipeType() {
        return type;
    }
    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Nullable
    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }
    @Override
    public abstract void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull T t, @NotNull IFocusGroup iFocusGroup);
    @Override
    public abstract void draw(@NotNull T recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY);
    public void addTooltips(GuiGraphics guiGraphics, int width, int height, Component[] components, int x, int y,double mouseX, double mouseY){
        Font font = Minecraft.getInstance().font;
        List<FormattedCharSequence> list = new ArrayList<>();

        if(mouseX >= x && mouseY >= y && mouseX <= width + x && mouseY <= height + y) {
            for (Component component : components) {
                list.add(component.getVisualOrderText());
            }

            guiGraphics.renderTooltip(font,list,(int) mouseX, (int) mouseY);
        }
    }

    public void addEnergyBarTooltip(GuiGraphics guiGraphics, Class<?> clazz,int width, int height, int x, int y,double mouseX, double mouseY){
        int powerPerTick = 0, maxProgress = 0;
        try {
            for (int i = 0; i < clazz.getFields().length; i++) {
                Field field = clazz.getFields()[i];
                if(field.getName().equals("powerPerTick")){
                    powerPerTick = field.getInt(field.getName());
                }else if(field.getName().equals("maxProgress")){
                    maxProgress = field.getInt(field.getName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addTooltips(guiGraphics,width,height,new Component[]{
                        Component.translatable("jei.cdp2.power").withStyle(ChatFormatting.GOLD).append(Component.literal(String.valueOf(powerPerTick)).withStyle(ChatFormatting.WHITE)).append(Component.literal(" FE/tick").withStyle(ChatFormatting.DARK_AQUA)),
                        Component.translatable("jei.cdp2.progress").withStyle(ChatFormatting.GOLD).append(Component.literal(String.valueOf(maxProgress)).withStyle(ChatFormatting.WHITE)).append(Component.literal(" tick").withStyle(ChatFormatting.DARK_AQUA)),
                        Component.translatable("jei.cdp2.total").withStyle(ChatFormatting.GOLD).append(Component.literal(String.valueOf(maxProgress * powerPerTick)).withStyle(ChatFormatting.WHITE)).append(Component.literal(" FE").withStyle(ChatFormatting.DARK_AQUA))},
                x,y,mouseX,mouseY
        );
    }

    public void addEnergyBarTooltip(ITooltipBuilder tooltip, double mouseX, double mouseY, int stored, int capacity){
        Rectangle rec = DefaultAssetProvider.DEFAULT_PROVIDER.getAsset(AssetTypes.ENERGY_BACKGROUND).getArea();
        if (new Rectangle(0, 12, rec.width, rec.height).contains(mouseX, mouseY)) {
            Component[] components = new Component[2];
            String s = String.valueOf(ChatFormatting.GOLD);
            components[0] = Component.literal(s + Component.translatable("tooltip.titanium.power").getString());
            s = (new DecimalFormat()).format((long)stored);
            components[1] = Component.literal(s + String.valueOf(ChatFormatting.GOLD) + "/" + String.valueOf(ChatFormatting.WHITE) + (new DecimalFormat()).format((long)capacity) + String.valueOf(ChatFormatting.DARK_AQUA) + " FE");
            tooltip.add(FormattedText.composite(components));
        }
    }

    public void addTooltipOnTexture(GuiGraphics guiGraphics, int width, int height, Component component, int x, int y,double mouseX, double mouseY){
        Font font = Minecraft.getInstance().font;

        if(mouseX >= x && mouseY >= y && mouseX <= width + x && mouseY <= height + y) {
            guiGraphics.renderTooltip(font, component, (int) mouseX, (int) mouseY);
        }
    }
    public void drawTextureWithTooltip(GuiGraphics guiGraphics, AllGuiTextures allGuiTextures, Component component, int x, int y, double mouseX, double mouseY){
        allGuiTextures.render(guiGraphics,x,y);
        addTooltipOnTexture(guiGraphics,allGuiTextures.width,allGuiTextures.height,component,x,y,mouseX,mouseY);
    }
    public IDrawable drawSlot(double chance){
        AllGuiTextures allJEITextures = null;

        if(chance >= 1){
            allJEITextures = AllGuiTextures.BASIC_SLOT;
        }else{
            allJEITextures = AllGuiTextures.CHANCE_SLOT;
        }

        AllGuiTextures finalAllJEITextures = allJEITextures;
        return new IDrawable() {
            @Override
            public int getWidth() {
                return finalAllJEITextures.width;
            }

            @Override
            public int getHeight() {
                return finalAllJEITextures.height;
            }

            @Override
            public void draw(@NotNull GuiGraphics guiGraphics, int i, int i1) {
                finalAllJEITextures.render(guiGraphics,i,i1);
            }
        };
    }

    public IRecipeSlotRichTooltipCallback addChanceTooltip(double chance){
        if(chance >= 1){
            return null;
        }
        return (view, tooltip) ->{
            tooltip.add(Component.translatable("jei.cdp2.chance", (chance >= 0.01 ? (int) (chance * 100) : "< 1") + "%").withStyle(ChatFormatting.GOLD));
        };
    }

    public IRecipeSlotRichTooltipCallback addText(String translatableKey, ChatFormatting style){
        return (view, tooltip) ->{
            tooltip.add(Component.translatable(translatableKey).withStyle(style));
        };
    }

    public IRecipeSlotRichTooltipCallback addLiteral(String context, ChatFormatting style){
        return (view, tooltip) ->{
            tooltip.add(Component.literal(context).withStyle(style));
        };
    }

    public void drawVoidBar(GuiGraphics guiGraphics,int x, int y, DyeColor dyeColor, double mouseX, double mouseY){
        IAssetProvider provider = IAssetProvider.DEFAULT_PROVIDER;
        IAsset assetBorder = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BORDER_VERTICAL);
        Point offset = assetBorder.getOffset();
        Rectangle area = assetBorder.getArea();
        guiGraphics.blit(assetBorder.getResourceLocation(), x + offset.x, y + offset.y, area.x, area.y, area.width, area.height);
        float[] colors = ProgressBarComponent.getTextureDiffuseColors(dyeColor);
        guiGraphics.setColor(colors[0], colors[1], colors[2], 1.0F);
        IAsset assetBar = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_BACKGROUND_VERTICAL);
        offset = assetBar.getOffset();
        area = assetBar.getArea();
        guiGraphics.blit(assetBar.getResourceLocation(), x + offset.x, y + offset.y, area.x, area.y, area.width, area.height);
        IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.PROGRESS_BAR_VERTICAL);
        offset = asset.getOffset();
        area = asset.getArea();
        int progress = 100;
        int maxProgress = 1000;
        int progressOffset = progress * area.height / Math.max(maxProgress, 1);
        guiGraphics.blit(asset.getResourceLocation(), offset.x + x, offset.y + area.height - progressOffset + y, area.x, area.y + (area.height - progressOffset), area.width, progressOffset);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        Rectangle r = assetBorder.getArea();
        addTooltips(guiGraphics,r.width,r.height,
                new Component[]{
                    Component.literal("Consume:").withStyle(ChatFormatting.GOLD),
                    Component.literal("100" + String.valueOf(ChatFormatting.GOLD) + "/" + String.valueOf(ChatFormatting.WHITE) + "1000 " + String.valueOf(ChatFormatting.DARK_AQUA) + "Matter")
                },x,y,mouseX,mouseY
        );
    }
}
