package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.LaserRecipe;
import de.ellpeck.actuallyadditions.mod.jei.laser.LaserRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LaserRecipeCategory.class)
public abstract class LaserRecipeCategoryMixin implements IRecipeCategory<LaserRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull LaserRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.LASER.get(), recipe);
    }
}
