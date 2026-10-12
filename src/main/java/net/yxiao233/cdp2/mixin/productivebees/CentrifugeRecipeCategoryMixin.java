package net.yxiao233.cdp2.mixin.productivebees;

import cy.jdkdigital.productivebees.common.recipe.CentrifugeRecipe;
import cy.jdkdigital.productivebees.compat.jei.CentrifugeRecipeCategory;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CentrifugeRecipeCategory.class)
public abstract class CentrifugeRecipeCategoryMixin implements IRecipeCategory<CentrifugeRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull CentrifugeRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.CENTRIFUGE_TYPE.get(), recipe);
    }
}
