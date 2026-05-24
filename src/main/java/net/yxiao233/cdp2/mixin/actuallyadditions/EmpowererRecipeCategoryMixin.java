package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.EmpowererRecipe;
import de.ellpeck.actuallyadditions.mod.jei.empowerer.EmpowererRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmpowererRecipeCategory.class)
public abstract class EmpowererRecipeCategoryMixin implements IRecipeCategory<EmpowererRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull EmpowererRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.EMPOWERING.get(), recipe);
    }
}
