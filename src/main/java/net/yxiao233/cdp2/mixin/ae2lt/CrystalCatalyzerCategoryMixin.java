package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.CrystalCatalyzerCategory;
import com.moakiee.ae2lt.machine.crystalcatalyzer.recipe.CrystalCatalyzerRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CrystalCatalyzerCategory.class)
public abstract class CrystalCatalyzerCategoryMixin implements IRecipeCategory<CrystalCatalyzerRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull CrystalCatalyzerRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.CRYSTAL_CATALYZER_TYPE.get(), recipe);
    }
}
