package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.FirmamentConversionCategory;
import com.moakiee.ae2lt.machine.firmament.recipe.FirmamentConversionRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FirmamentConversionCategory.class)
public abstract class FirmamentConversionCategoryMixin implements IRecipeCategory<FirmamentConversionRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull FirmamentConversionRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.FIRMAMENT_CONVERSION_TYPE.get(), recipe);
    }
}
