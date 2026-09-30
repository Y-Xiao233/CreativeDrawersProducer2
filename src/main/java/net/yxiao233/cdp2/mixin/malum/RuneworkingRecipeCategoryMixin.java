package net.yxiao233.cdp2.mixin.malum;

import com.sammy.malum.common.recipe.RuneworkingRecipe;
import com.sammy.malum.compat.jei.categories.RuneworkingRecipeCategory;
import com.sammy.malum.registry.common.recipe.MalumRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RuneworkingRecipeCategory.class)
public abstract class RuneworkingRecipeCategoryMixin implements IRecipeCategory<RuneworkingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull RuneworkingRecipe recipe) {
        return RecipeUtil.getRecipeId(MalumRecipeTypes.RUNEWORKING.get(),recipe);
    }
}
