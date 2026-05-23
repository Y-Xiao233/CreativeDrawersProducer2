package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.ReprocessorCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ReprocessorCategory.class)
public abstract class ReprocessorCategoryMixin implements IRecipeCategory<IReprocessorRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull IReprocessorRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.REPROCESSOR.get(), recipe);
    }
}
