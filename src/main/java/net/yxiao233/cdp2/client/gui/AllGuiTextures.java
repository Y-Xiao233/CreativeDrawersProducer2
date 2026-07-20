package net.yxiao233.cdp2.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public enum AllGuiTextures implements ScreenElement{
    //Empty
    EMPTY("empty",0,0,0,0),
    //Slot
    CHANCE_SLOT(20, 156, 18, 18),
    BASIC_SLOT(0,0,18,18),
    //PROGRESS
    ETERNA(57,36,110,5),
    ETERNA_BORDER(57,52,110,5),
    QUANTA(57,41,110,5),
    QUANTA_BORDER(57,57,110,5),
    ARCANA(57,46,110,5),
    ARCANA_BORDER(57,62,110,5),
    //Information
    JEI_INFORMATION(240,0,16,16);
    public final ResourceLocation location;
    public final int width, height;
    public final int startX, startY;
    private AllGuiTextures(String location, int startX, int startY, int width, int height) {
        this.location = ResourceLocation.fromNamespaceAndPath(CreativeDrawersProducer2.MODID, "textures/gui/" + location + ".png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    private AllGuiTextures(int startX, int startY, int width, int height) {
        this.location = ResourceLocation.fromNamespaceAndPath(CreativeDrawersProducer2.MODID, "textures/gui/widgets.png");
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(location, x, y, startX, startY, width, height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int extraValue, ExtraType extraType) {
        if(extraType == ExtraType.HEIGHT){
            guiGraphics.blit(location, x, y, startX, startY, width, extraValue);
        }else{
            guiGraphics.blit(location, x, y, startX, startY, extraValue, height);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.blit(location, x, y, startX, startY, width, height);
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
