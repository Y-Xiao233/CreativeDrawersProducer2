package net.yxiao233.cdp2.mixin.actuallyadditions;

import de.ellpeck.actuallyadditions.mod.crafting.ActuallyRecipes;
import de.ellpeck.actuallyadditions.mod.crafting.PressingRecipe;
import de.ellpeck.actuallyadditions.mod.jei.pressing.PressingCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PressingCategory.class)
public abstract class PressingCategoryMixin implements IRecipeCategory<PressingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull PressingRecipe recipe) {
        return RecipeUtil.getRecipeId(ActuallyRecipes.Types.PRESSING.get(), recipe);
    }
}
