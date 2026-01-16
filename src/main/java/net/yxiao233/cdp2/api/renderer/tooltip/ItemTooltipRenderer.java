package net.yxiao233.cdp2.api.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.cdp2.util.RendererUtil;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ItemTooltipRenderer implements ClientTooltipComponent {
    private final ItemTooltipComponent component;
    public ItemTooltipRenderer(ItemTooltipComponent component){
        this.component = component;
    }
    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 18;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        guiGraphics.renderFakeItem(component.stack(),x,y);
        RendererUtil.renderSizeLabel(guiGraphics, font, x, y, "2.14G", false);
    }
}
