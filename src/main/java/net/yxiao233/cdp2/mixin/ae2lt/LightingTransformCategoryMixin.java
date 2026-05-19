package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.LightningTransformCategory;
import com.moakiee.ae2lt.lightning.LightningTransformRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningTransformCategory.class)
public abstract class LightingTransformCategoryMixin implements IRecipeCategory<LightningTransformRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull LightningTransformRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.LIGHTNING_TRANSFORM_TYPE.get(), recipe);
    }
}
