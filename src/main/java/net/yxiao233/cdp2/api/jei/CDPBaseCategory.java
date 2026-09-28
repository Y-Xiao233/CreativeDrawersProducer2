package net.yxiao233.cdp2.api.jei;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.component.progress.ProgressBarComponent;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.yxiao233.industrialforegoingextra.api.jei.AbstractJEICategory;

import java.awt.*;

public abstract class CDPBaseCategory<T> extends AbstractJEICategory<T> {
    public CDPBaseCategory(IGuiHelper helper, RecipeType<T> type, Component title, Item icon, int width, int height) {
        super(helper, type, title, icon, width, height);
    }
    public IRecipeSlotRichTooltipCallback addLiteral(String context, ChatFormatting style){
        return (view, tooltip) ->{
            tooltip.add(Component.literal(context).withStyle(style));
        };
    }

    public void drawVoidBar(GuiGraphics guiGraphics, int x, int y, DyeColor dyeColor, double mouseX, double mouseY){
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
