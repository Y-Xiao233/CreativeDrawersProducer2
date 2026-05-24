package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.module.ModuleCore;
import com.buuz135.industrial.plugin.jei.category.StoneWorkGeneratorCategory;
import com.buuz135.industrial.recipe.StoneWorkGenerateRecipe;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StoneWorkGeneratorCategory.class)
public abstract class StoneWorkGeneratorCategoryMixin implements IRecipeCategory<StoneWorkGenerateRecipe> {
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable ResourceLocation getRegistryName(@NotNull StoneWorkGenerateRecipe recipe) {
        return RecipeUtil.getRecipeId((RecipeType<StoneWorkGenerateRecipe>) ModuleCore.STONEWORK_GENERATE_TYPE.get(), recipe);
    }
}
