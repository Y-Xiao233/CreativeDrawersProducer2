package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.OverloadProcessingCategory;
import com.moakiee.ae2lt.machine.overloadfactory.recipe.OverloadProcessingRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverloadProcessingCategory.class)
public abstract class OverloadProcessingRecipeMixin implements IRecipeCategory<OverloadProcessingRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull OverloadProcessingRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.OVERLOAD_PROCESSING_TYPE.get(), recipe);
    }
}
