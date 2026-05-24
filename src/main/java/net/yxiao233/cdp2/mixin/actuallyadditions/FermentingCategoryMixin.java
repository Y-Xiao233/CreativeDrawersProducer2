package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.FermentingRecipe;
import de.ellpeck.actuallyadditions.mod.jei.fermenting.FermentingCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FermentingCategory.class)
public abstract class FermentingCategoryMixin implements IRecipeCategory<FermentingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull FermentingRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.FERMENTING.get(), recipe);
    }
}
