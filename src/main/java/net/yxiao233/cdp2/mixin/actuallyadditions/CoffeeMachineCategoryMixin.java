package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.CoffeeIngredientRecipe;
import de.ellpeck.actuallyadditions.mod.jei.coffee.CoffeeMachineCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CoffeeMachineCategory.class)
public abstract class CoffeeMachineCategoryMixin implements IRecipeCategory<CoffeeIngredientRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull CoffeeIngredientRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.COFFEE_INGREDIENT.get(), recipe);
    }
}
