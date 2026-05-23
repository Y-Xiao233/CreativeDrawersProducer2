package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.IInfusionRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.InfusionCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InfusionCategory.class)
public abstract class InfusionCategoryMixin implements IRecipeCategory<IInfusionRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull IInfusionRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.INFUSION.get(),recipe);
    }
}
