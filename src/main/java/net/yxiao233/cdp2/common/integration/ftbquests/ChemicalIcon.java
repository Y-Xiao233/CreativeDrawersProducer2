package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.icon.IResourceIcon;
import dev.ftb.mods.ftblibrary.icon.Icon;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.gui.GuiUtils;
import mekanism.client.render.MekanismRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.text.NumberFormat;
import java.util.Locale;

public class ChemicalIcon extends Icon implements IResourceIcon {
    private final ChemicalStack stack;
    public ChemicalIcon(ChemicalStack stack){
        this.stack = stack;
    }
    @Override
    public void draw(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        if (!stack.isEmpty()) {
            MekanismRenderer.color(guiGraphics, stack);
            GuiUtils.drawTiledSprite(guiGraphics, x, y, h, w, h, MekanismRenderer.getChemicalTexture(stack), 16, 16, 100, GuiUtils.TilingDirection.UP_RIGHT);
            MekanismRenderer.resetColor(guiGraphics);
        }
    }

    private String formatAmount(long amount){
        NumberFormat formatter = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        formatter.setMaximumFractionDigits(1);
        return formatter.format(amount);
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return MekanismAPI.CHEMICAL_REGISTRY.getKey(this.stack.getChemical());
    }
}
