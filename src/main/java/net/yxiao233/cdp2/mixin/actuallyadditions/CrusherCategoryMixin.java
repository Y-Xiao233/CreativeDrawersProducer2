package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.CrushingRecipe;
import de.ellpeck.actuallyadditions.mod.jei.crusher.CrusherCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrusherCategory.class)
public abstract class CrusherCategoryMixin implements IRecipeCategory<CrushingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull CrushingRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.CRUSHING.get(), recipe);
    }
}
