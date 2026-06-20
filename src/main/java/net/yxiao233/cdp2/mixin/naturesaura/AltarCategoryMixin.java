package net.yxiao233.cdp2.mixin.naturesaura;

import de.ellpeck.naturesaura.compat.jei.AltarCategory;
import de.ellpeck.naturesaura.recipes.AltarRecipe;
import de.ellpeck.naturesaura.recipes.ModRecipes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AltarCategory.class)
public abstract class AltarCategoryMixin implements IRecipeCategory<AltarRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull AltarRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipes.ALTAR_TYPE, recipe);
    }

    @Override
    public void draw(@NotNull AltarRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("jei.cdp2.needed_aura",recipe.aura),0,48,0x7CFC00);
    }
}
