package net.yxiao233.cdp2.mixin.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import com.blakebr0.mysticalagriculture.compat.jei.category.SouliumSpawnerCategory;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SouliumSpawnerCategory.class)
public abstract class SouliumSpawnerCategoryMixin implements IRecipeCategory<ISouliumSpawnerRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull ISouliumSpawnerRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.SOULIUM_SPAWNER.get(), recipe);
    }
}
