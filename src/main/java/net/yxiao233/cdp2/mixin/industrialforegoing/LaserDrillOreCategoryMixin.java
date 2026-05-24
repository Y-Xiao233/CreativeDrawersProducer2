package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.category.LaserDrillOreCategory;
import com.buuz135.industrial.recipe.LaserDrillOreRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LaserDrillOreCategory.class)
public abstract class LaserDrillOreCategoryMixin implements IRecipeCategory<LaserDrillOreRecipe> {
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable ResourceLocation getRegistryName(@NotNull LaserDrillOreRecipe recipe) {
        return RecipeUtil.getRecipeId((RecipeType<LaserDrillOreRecipe>) ModuleCore.LASER_DRILL_TYPE.get(), recipe);
    }
}
