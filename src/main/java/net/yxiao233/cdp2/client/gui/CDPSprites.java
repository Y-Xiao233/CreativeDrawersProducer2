package net.yxiao233.cdp2.client.gui;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public class CDPSprites {
    public static ResourceLocation TEXTURES = CreativeDrawersProducer2.makeId("textures/gui/gui.png");
    public static IGuiTexture INPUT_SLOT = SpriteTexture.of(TEXTURES).setSprite(0,0,13,13).setBorder(4,4,4,4);
    public static IGuiTexture OUTPUT_SLOT = SpriteTexture.of(TEXTURES).setSprite(14,0,13,13).setBorder(4,4,4,4);
    public static IGuiTexture CATALYST_SLOT = SpriteTexture.of(TEXTURES).setSprite(28,0,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RESET_BUTTON = SpriteTexture.of(TEXTURES).setSprite(0,28,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RESET_BUTTON_DARK = SpriteTexture.of(TEXTURES).setSprite(0,42,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RESET_BUTTON_LIGHT = SpriteTexture.of(TEXTURES).setSprite(0,14,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RANGE_BUTTON = SpriteTexture.of(TEXTURES).setSprite(14,28,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RANGE_BUTTON_DARK = SpriteTexture.of(TEXTURES).setSprite(14,42,13,13).setBorder(4,4,4,4);
    public static IGuiTexture RANGE_BUTTON_LIGHT = SpriteTexture.of(TEXTURES).setSprite(14,14,13,13).setBorder(4,4,4,4);
    public static IGuiTexture INFORMATION_BUTTON = SpriteTexture.of(TEXTURES).setSprite(28,28,13,13).setBorder(4,4,4,4);
    public static IGuiTexture INFORMATION_BUTTON_DARK = SpriteTexture.of(TEXTURES).setSprite(28,42,13,13).setBorder(4,4,4,4);
    public static IGuiTexture INFORMATION_BUTTON_LIGHT = SpriteTexture.of(TEXTURES).setSprite(28,14,13,13).setBorder(4,4,4,4);
}
