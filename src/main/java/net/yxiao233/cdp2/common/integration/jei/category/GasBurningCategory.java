package net.yxiao233.cdp2.common.integration.jei.category;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.datamaps.IMekanismDataMapTypes;
import mekanism.api.datamaps.chemical.attribute.ChemicalFuel;
import mekanism.client.recipe_viewer.jei.MekanismJEIHelper;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.yxiao233.cdp2.api.jei.CDPBaseCategory;
import net.yxiao233.cdp2.api.jei.CDPJeiCategory;
import net.yxiao233.cdp2.common.integration.jei.CDPRecipeType;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CDPJeiCategory
public class GasBurningCategory extends CDPBaseCategory<GasBurningCategory.GasBurningRecipeWrapper> {
    public static final Component TITLE = Component.translatable("jei.cdp2.gas_burning");
    public GasBurningCategory(IGuiHelper helper) {
        super(helper, CDPRecipeType.GAS_BURNING, TITLE, GeneratorsBlocks.GAS_BURNING_GENERATOR.asItem(), 120, 70);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull GasBurningCategory.GasBurningRecipeWrapper recipe, @NotNull IFocusGroup iFocusGroup) {
        builder.addInputSlot(51,6).addIngredient(MekanismJEIHelper.INSTANCE.getChemicalStackHelper().getIngredientType(), recipe.chemicalStack);
    }

    @Override
    public void draw(@NotNull GasBurningCategory.GasBurningRecipeWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawSlot(1).draw(guiGraphics,50,5);
        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("jei.cdp2.gas_burning.burn_tick",recipe.burnTicks),10,25, 16777215);
        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("jei.cdp2.gas_burning.energy_per_tick", recipe.energyPerTick),10,40, 16777215);
        NumberFormat format = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        format.setMinimumFractionDigits(2);
        guiGraphics.drawString(Minecraft.getInstance().font,Component.translatable("jei.cdp2.gas_burning.energy_density", format.format(recipe.energyPerTick * recipe.burnTicks * 4 / 10)),10,55, 16777215);
    }

    @Override
    public void addRecipes(IRecipeRegistration registration) {
        List<GasBurningRecipeWrapper> recipes = new ArrayList<>();
        MekanismAPI.CHEMICAL_REGISTRY.forEach(chemical -> {
            ChemicalStack chemicalStack = new ChemicalStack(Holder.direct(chemical), 1000);
            boolean isFuel = chemicalStack.getData(IMekanismDataMapTypes.INSTANCE.chemicalFuel()) != null;
            if(isFuel){
                ChemicalFuel data = chemicalStack.getData(IMekanismDataMapTypes.INSTANCE.chemicalFuel());
                if(data != null){
                    recipes.add(new GasBurningRecipeWrapper(chemicalStack,data.burnTicks(),data.energyPerTick()));
                }
            }
        });
        registration.addRecipes(CDPRecipeType.GAS_BURNING,recipes);
    }

    public record GasBurningRecipeWrapper(ChemicalStack chemicalStack, int burnTicks, long energyPerTick) {

    }
}
