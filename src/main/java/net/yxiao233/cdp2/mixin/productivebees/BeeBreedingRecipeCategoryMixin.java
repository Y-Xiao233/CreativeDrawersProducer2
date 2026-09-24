package net.yxiao233.cdp2.mixin.productivebees;

import cy.jdkdigital.productivebees.common.recipe.BeeBreedingRecipe;
import cy.jdkdigital.productivebees.compat.jei.BeeBreedingRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BeeBreedingRecipeCategory.class)
public class BeeBreedingRecipeCategoryMixin {
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcy/jdkdigital/productivebees/common/recipe/BeeBreedingRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"))
    private void cdp2$setRecipe(IRecipeLayoutBuilder builder, BeeBreedingRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.parent1.get())));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.parent2.get())));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.offspring.get())));
    }
}
