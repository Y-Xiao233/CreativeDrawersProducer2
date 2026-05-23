package net.yxiao233.cdp2.mixin.naturesaura;

import de.ellpeck.naturesaura.compat.jei.TreeRitualCategory;
import de.ellpeck.naturesaura.recipes.ModRecipes;
import de.ellpeck.naturesaura.recipes.TreeRitualRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TreeRitualCategory.class)
public abstract class TreeRitualCategoryMixin implements IRecipeCategory<TreeRitualRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull TreeRitualRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipes.TREE_RITUAL_TYPE,recipe);
    }
}
