package net.yxiao233.cdp2.common.integration.jei.category;

import appeng.core.definitions.AEItems;
import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import mekanism.client.recipe_viewer.jei.MekanismJEIHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.common.integration.jei.CDPRecipeType;
import net.yxiao233.cdp2.common.recipe.ChemicalFromCellInfo;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChemicalFromCellInfoCategory extends CDPBaseCategory<ChemicalFromCellInfo>{
    public static final Component TITLE = Component.translatable("jei.cdp2.chemical_from_cell_info");
    public ChemicalFromCellInfoCategory(IGuiHelper helper) {
        super(helper, CDPRecipeType.CHEMICAL_FROM_CELL_INFO, TITLE, AEItems.ITEM_CELL_1K.get(), 68, 22);
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull ChemicalFromCellInfo recipe) {
        return RecipeUtil.getRecipeId(CDPRecipe.CHEMICAL_FROM_CELL_INFO.asType(), recipe);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ChemicalFromCellInfo info, @NotNull IFocusGroup iFocusGroup) {
        builder.addInputSlot(4,4).addIngredient(VanillaTypes.ITEM_STACK,info.cell);
        builder.addOutputSlot(50,4)
                .addIngredient(MekanismJEIHelper.INSTANCE.getChemicalStackHelper().getIngredientType(), info.chemical.copyWithAmount(1000))
                .addRichTooltipCallback(addLiteral(info.chemical.getAmount() + "mB", ChatFormatting.GRAY));
    }

    @Override
    public void draw(@NotNull ChemicalFromCellInfo recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        //ProgressBar
        AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 24,  4);
    }
}
