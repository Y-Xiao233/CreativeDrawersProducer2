package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.IAwakeningRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.AwakeningCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AwakeningCategory.class)
public abstract class AwakeningCategoryMixin implements IRecipeCategory<IAwakeningRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull IAwakeningRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.AWAKENING.get(), recipe);
    }
}
