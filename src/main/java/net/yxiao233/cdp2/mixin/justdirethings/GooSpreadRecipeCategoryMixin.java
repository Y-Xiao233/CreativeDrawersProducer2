package net.yxiao233.cdp2.mixin.justdirethings;

import com.direwolf20.justdirethings.client.jei.GooSpreadRecipeCategory;
import com.direwolf20.justdirethings.datagen.recipes.GooSpreadRecipe;
import com.direwolf20.justdirethings.setup.Registration;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GooSpreadRecipeCategory.class)
public abstract class GooSpreadRecipeCategoryMixin implements IRecipeCategory<GooSpreadRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull GooSpreadRecipe recipe) {
        return RecipeUtil.getRecipeId(Registration.GOO_SPREAD_RECIPE_TYPE.get(),recipe);
    }
}
