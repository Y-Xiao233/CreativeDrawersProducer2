package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.category.DissolutionChamberCategory;
import com.buuz135.industrial.recipe.DissolutionChamberRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DissolutionChamberCategory.class)
public abstract class DissolutionChamberCategoryMixin implements IRecipeCategory<DissolutionChamberRecipe> {
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable ResourceLocation getRegistryName(@NotNull DissolutionChamberRecipe recipe) {
        return RecipeUtil.getRecipeId((RecipeType<DissolutionChamberRecipe>) ModuleCore.DISSOLUTION_TYPE.get(), recipe);
    }
}
