package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.LightningStrikeCategory;
import com.moakiee.ae2lt.lightning.strike.LightningStrikeRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningStrikeCategory.class)
public abstract class LightingStrikeCategoryMixin implements IRecipeCategory<LightningStrikeRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull LightningStrikeRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.LIGHTNING_STRIKE_TYPE.get(), recipe);
    }
}
