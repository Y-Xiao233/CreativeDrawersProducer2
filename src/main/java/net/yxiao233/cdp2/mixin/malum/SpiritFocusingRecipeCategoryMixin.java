package net.yxiao233.cdp2.mixin.malum;

import com.sammy.malum.common.recipe.SpiritFocusingRecipe;
import com.sammy.malum.compat.jei.categories.SpiritFocusingRecipeCategory;
import com.sammy.malum.registry.common.recipe.MalumRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiritFocusingRecipeCategory.class)
public abstract class SpiritFocusingRecipeCategoryMixin  implements IRecipeCategory<SpiritFocusingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull SpiritFocusingRecipe recipe) {
        return RecipeUtil.getRecipeId(MalumRecipeTypes.SPIRIT_FOCUSING.get(),recipe);
    }
}
