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
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.yxiao233.cdp2.api.jei.CDPBaseCategory;
import net.yxiao233.cdp2.api.jei.CDPJeiCategory;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.common.integration.jei.CDPRecipeType;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@CDPJeiCategory
public class CreativeDrawerInfoCategory extends CDPBaseCategory<CreativeDrawerInfo> {
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

    @Override
    public void addRecipes(IRecipeRegistration registration) {
        List<CreativeDrawerInfo> recipes = new ArrayList<>();
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(register -> {
            ItemStack drawer = register.asStack();
            ItemStack infinityItem =  ((CreativeDrawerBlock) register.asBlock()).getInfinityItem().get();
            recipes.add(new CreativeDrawerInfo(drawer,infinityItem));
        });

        registration.addRecipes(CDPRecipeType.DRAWER_INFO,recipes);
    }
}
