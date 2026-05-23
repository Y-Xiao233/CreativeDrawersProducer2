package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.IEnchanterRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.EnchanterCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchanterCategory.class)
public abstract class EnchanterCategoryMixin implements IRecipeCategory<IEnchanterRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull IEnchanterRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.ENCHANTER.get(), recipe);
    }
}
