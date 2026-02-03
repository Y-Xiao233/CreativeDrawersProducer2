package net.yxiao233.cdp2.common.integration.jei.category;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.client.screen.asset.DefaultAssetProvider;
import com.hrznstudio.titanium.client.screen.asset.IAssetProvider;
import com.hrznstudio.titanium.util.AssetUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.yxiao233.cdp2.common.integration.jei.CDPRecipeType;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

public class CreativeDrawerInfoCategory extends CDPBaseCategory<CreativeDrawerInfo>{
    public static final Component TITLE = Component.translatable("jei.cdp2.creative_drawer_info");
    public CreativeDrawerInfoCategory(IGuiHelper helper) {
        super(helper, CDPRecipeType.DRAWER_INFO, TITLE, CDPBlock.VOID_MATTER_CREATIVE_DRAWER.asItem(),66, 22);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CreativeDrawerInfo info, @NotNull IFocusGroup iFocusGroup) {
        builder.addInputSlot(4,4).addIngredient(VanillaTypes.ITEM_STACK,info.drawer);
        builder.addOutputSlot(48,4).addIngredient(VanillaTypes.ITEM_STACK,info.infinityItem);
    }

    @Override
    public void draw(@NotNull CreativeDrawerInfo recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        //ProgressBar
        AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 24,  4);
    }
}
