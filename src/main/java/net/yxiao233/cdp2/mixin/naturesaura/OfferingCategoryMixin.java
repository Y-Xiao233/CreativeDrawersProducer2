package net.yxiao233.cdp2.mixin.naturesaura;

import de.ellpeck.naturesaura.compat.jei.OfferingCategory;
import de.ellpeck.naturesaura.recipes.ModRecipes;
import de.ellpeck.naturesaura.recipes.OfferingRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OfferingCategory.class)
public abstract class OfferingCategoryMixin implements IRecipeCategory<OfferingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull OfferingRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipes.OFFERING_TYPE,recipe);
    }
}
