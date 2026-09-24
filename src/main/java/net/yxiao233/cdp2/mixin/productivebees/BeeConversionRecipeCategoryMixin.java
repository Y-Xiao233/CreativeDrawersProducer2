package net.yxiao233.cdp2.mixin.productivebees;

import cy.jdkdigital.productivebees.common.recipe.BeeConversionRecipe;
import cy.jdkdigital.productivebees.compat.jei.BeeConversionRecipeCategory;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BeeConversionRecipeCategory.class)
public abstract class BeeConversionRecipeCategoryMixin implements IRecipeCategory<BeeConversionRecipe> {

    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;Lcy/jdkdigital/productivebees/common/recipe/BeeConversionRecipe;Lmezz/jei/api/recipe/IFocusGroup;)V", at = @At("TAIL"))
    private void cdp2$setRecipe(IRecipeLayoutBuilder builder, BeeConversionRecipe recipe, IFocusGroup focuses, CallbackInfo ci){
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.source.get())));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.result.get())));
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull BeeConversionRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.BEE_CONVERSION_TYPE.get(), recipe);
    }
}
