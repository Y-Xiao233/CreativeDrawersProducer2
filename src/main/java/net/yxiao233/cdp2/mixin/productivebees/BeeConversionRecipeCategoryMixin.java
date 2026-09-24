package net.yxiao233.cdp2.mixin.productivebees;

import cy.jdkdigital.productivebees.common.recipe.BeeConversionRecipe;
import cy.jdkdigital.productivebees.compat.jei.BeeConversionRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BeeConversionRecipeCategory.class)
public class BeeConversionRecipeCategoryMixin{

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcy/jdkdigital/productivebees/common/recipe/BeeConversionRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"))
    private void cdp2$setRecipe(IRecipeLayoutBuilder builder, BeeConversionRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.source.get())));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.result.get())));
    }
}
