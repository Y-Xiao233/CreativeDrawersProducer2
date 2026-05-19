package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.integration.jei.category.LightningSimulationCategory;
import com.moakiee.ae2lt.machine.lightningchamber.recipe.LightningSimulationRecipe;
import com.moakiee.ae2lt.registry.ModRecipeTypes;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningSimulationCategory.class)
public abstract class LightningSimulationCategoryMixin implements IRecipeCategory<LightningSimulationRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull LightningSimulationRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipeTypes.LIGHTNING_SIMULATION_TYPE.get(), recipe);
    }
}
