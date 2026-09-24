package net.yxiao233.cdp2.mixin.productivebees;

import cy.jdkdigital.productivebees.common.recipe.BeeSpawningRecipe;
import cy.jdkdigital.productivebees.compat.jei.BeeSpawningRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BeeSpawningRecipeCategory.class)
public class BeeSpawningRecipeCategoryMixin {
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcy/jdkdigital/productivebees/common/recipe/BeeSpawningRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"))
    private void cdp2$setRecipe(IRecipeLayoutBuilder builder, BeeSpawningRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        recipe.output.forEach(result ->{
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(result.get())));
        });
    }
}
