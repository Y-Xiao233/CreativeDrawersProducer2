package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.category.FluidExtractorCategory;
import com.buuz135.industrial.recipe.FluidExtractorRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidExtractorCategory.class)
public abstract class FluidExtractorCategoryMixin implements IRecipeCategory<FluidExtractorRecipe> {
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable ResourceLocation getRegistryName(@NotNull FluidExtractorRecipe recipe) {
        return RecipeUtil.getRecipeId((RecipeType<FluidExtractorRecipe>) ModuleCore.FLUID_EXTRACTOR_TYPE.get(), recipe);
    }
}
