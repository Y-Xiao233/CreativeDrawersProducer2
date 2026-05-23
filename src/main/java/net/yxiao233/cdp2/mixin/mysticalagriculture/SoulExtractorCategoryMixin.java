package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.SoulExtractorCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SoulExtractorCategory.class)
public abstract class SoulExtractorCategoryMixin implements IRecipeCategory<ISoulExtractionRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull ISoulExtractionRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.SOUL_EXTRACTION.get(), recipe);
    }
}
