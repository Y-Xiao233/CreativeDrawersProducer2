package net.yxiao233.cdp2.mixin.malum;

import com.sammy.malum.common.recipe.VoidFavorRecipe;
import com.sammy.malum.compat.jei.categories.WeepingWellRecipeCategory;
import com.sammy.malum.registry.common.recipe.MalumRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WeepingWellRecipeCategory.class)
public abstract class WeepingWellRecipeCategoryMixin implements IRecipeCategory<VoidFavorRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull VoidFavorRecipe recipe) {
        return RecipeUtil.getRecipeId(MalumRecipeTypes.VOID_FAVOR.get(),recipe);
    }
}
