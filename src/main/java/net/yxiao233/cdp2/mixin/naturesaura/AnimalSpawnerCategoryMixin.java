package net.yxiao233.cdp2.mixin.naturesaura;

import de.ellpeck.naturesaura.compat.jei.AnimalSpawnerCategory;
import de.ellpeck.naturesaura.recipes.AnimalSpawnerRecipe;
import de.ellpeck.naturesaura.recipes.ModRecipes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AnimalSpawnerCategory.class)
public abstract class AnimalSpawnerCategoryMixin implements IRecipeCategory<AnimalSpawnerRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull AnimalSpawnerRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipes.ANIMAL_SPAWNER_TYPE, recipe);
    }
}
