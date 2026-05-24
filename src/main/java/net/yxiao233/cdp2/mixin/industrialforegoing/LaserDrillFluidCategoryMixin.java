package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.category.LaserDrillFluidCategory;
import com.buuz135.industrial.recipe.LaserDrillFluidRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LaserDrillFluidCategory.class)
public abstract class LaserDrillFluidCategoryMixin implements IRecipeCategory<LaserDrillFluidRecipe> {
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable ResourceLocation getRegistryName(@NotNull LaserDrillFluidRecipe recipe) {
        return RecipeUtil.getRecipeId((RecipeType<LaserDrillFluidRecipe>) ModuleCore.LASER_DRILL_FLUID_TYPE.get(), recipe);
    }
}
