package net.yxiao233.cdp2.mixin.malum;

import com.sammy.malum.common.recipe.spirit_repair.SpiritRepairRecipe;
import com.sammy.malum.compat.jei.categories.SpiritRepairRecipeCategory;
import com.sammy.malum.registry.common.recipe.MalumRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpiritRepairRecipeCategory.class)
public abstract class SpiritRepairRecipeCategoryMixin implements IRecipeCategory<SpiritRepairRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull SpiritRepairRecipe recipe) {
        return RecipeUtil.getRecipeId(MalumRecipeTypes.SPIRIT_REPAIR.get(),recipe);
    }
}
