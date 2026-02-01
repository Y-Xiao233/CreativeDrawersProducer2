package net.yxiao233.cdp2.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ScreenElement {
    @OnlyIn(Dist.CLIENT)
    void render(GuiGraphics guiGraphics, int x, int y);
    void render(GuiGraphics guiGraphics, int x, int y, int extraValue,ExtraType extraType);
    void render(GuiGraphics guiGraphics, int x, int y, int width, int height);
    int getWidth();
    int getHeight();

    public enum ExtraType{
        HEIGHT,
        WIDTH;
    }
}
