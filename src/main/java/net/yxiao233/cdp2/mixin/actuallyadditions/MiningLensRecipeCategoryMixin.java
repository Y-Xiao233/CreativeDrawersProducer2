package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.MiningLensRecipe;
import de.ellpeck.actuallyadditions.mod.jei.lens.MiningLensRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MiningLensRecipeCategory.class)
public abstract class MiningLensRecipeCategoryMixin implements IRecipeCategory<MiningLensRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull MiningLensRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.MINING_LENS.get(), recipe);
    }
}
