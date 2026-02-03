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
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.client.gui.AllGuiTextures;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.integration.jei.CDPRecipeType;
import org.jetbrains.annotations.NotNull;

public class VoidSieveCategory extends CDPBaseCategory<VoidSieveRecipe> {
    public static final Component TITLE = Component.translatable("block.cdp2.void_sieve");
    public VoidSieveCategory(IGuiHelper helper) {
        super(helper, CDPRecipeType.VOID_SIEVE, TITLE, CDPBlock.VOID_SIEVE.asItem(), 160, 82);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull VoidSieveRecipe voidSieveRecipe, @NotNull IFocusGroup iFocusGroup) {
        ChanceIngredient chanceIngredient = voidSieveRecipe.input;
        if(chanceIngredient.chance() >= 1){
            builder.addInputSlot(43,32).addIngredients(chanceIngredient.sizedIngredient().ingredient()).setBackground(drawSlot(chanceIngredient.chance()),-1,-1);
        }else{
            builder.addInputSlot(43,32).addIngredients(chanceIngredient.sizedIngredient().ingredient()).setBackground(drawSlot(chanceIngredient.chance()),-1,-1).addRichTooltipCallback(addChanceTooltip(chanceIngredient.chance()));
        }

        int x = 103;
        int y = 17;
        for (int i = 0; i < voidSieveRecipe.outputs.size(); i++) {
            ChanceItemStack chanceItemStack = voidSieveRecipe.outputs.get(i);
            if(i % 3 == 0 && i != 0){
                x = 103;
                y += 18;
            }
            if(chanceItemStack.chance() >= 1){
                builder.addOutputSlot(x,y).addIngredient(VanillaTypes.ITEM_STACK,chanceItemStack.item());
            }else{
                builder.addOutputSlot(x,y).addIngredient(VanillaTypes.ITEM_STACK,chanceItemStack.item()).addRichTooltipCallback(addChanceTooltip(chanceItemStack.chance()));
            }
            x += 18;
        }

        builder.addSlot(RecipeIngredientRole.CATALYST,17,64).addIngredient(VanillaTypes.ITEM_STACK, CDPItem.VOID_MATTER.asStack()).setBackground(drawSlot(1),-1,-1);
    }

    @Override
    public void draw(@NotNull VoidSieveRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        //Output
        int x = 102;
        int y = 16;
        for (int i = 0; i < 9; i++) {
            if(i % 3 == 0 && i != 0){
                x = 102;
                y += 18;
            }
            if(recipe.outputs.size() > i){
                double chance = recipe.outputs.get(i).chance();
                if(chance >= 1){
                    AllGuiTextures.BASIC_SLOT.render(guiGraphics,x,y);
                }else{
                    AllGuiTextures.CHANCE_SLOT.render(guiGraphics,x,y);
                }
            }else{
                AllGuiTextures.BASIC_SLOT.render(guiGraphics,x,y);
            }
            x += 18;
        }
        //ProgressBar
        AssetUtil.drawAsset(guiGraphics, Minecraft.getInstance().screen, IAssetProvider.getAsset(DefaultAssetProvider.DEFAULT_PROVIDER, AssetTypes.PROGRESS_BAR_BACKGROUND_ARROW_HORIZONTAL), 68, 41 - 8);
        //void bar
        drawVoidBar(guiGraphics,20,4,DyeColor.PURPLE,mouseX,mouseY);
    }
}
