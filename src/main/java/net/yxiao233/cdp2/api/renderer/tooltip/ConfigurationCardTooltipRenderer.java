package net.yxiao233.cdp2.api.renderer.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ConfigurationCardTooltipRenderer implements ClientTooltipComponent {
    private final ConfigurationCardTooltipComponent component;
    public ConfigurationCardTooltipRenderer(ConfigurationCardTooltipComponent component){
        this.component = component;
    }
    @Override
    public int getHeight() {
        return 18 * 2 + 4;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 18;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {
        guiGraphics.renderFakeItem(component.seed(),x,y);
        guiGraphics.renderFakeItem(component.soil(),x,y + 18);
    }
}
