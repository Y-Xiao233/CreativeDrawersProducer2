package net.yxiao233.cdp2.mixin.advancedae;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import net.pedroksl.advanced_ae.xmod.jei.ReactionChamberCategory;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReactionChamberCategory.class)
public abstract class ReactionChamberCategoryMixin implements IRecipeCategory<ReactionChamberRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull ReactionChamberRecipe recipe) {
        return RecipeUtil.getRecipeId(ReactionChamberRecipe.TYPE,recipe);
    }
}
