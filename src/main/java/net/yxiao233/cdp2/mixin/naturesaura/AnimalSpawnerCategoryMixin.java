package net.yxiao233.cdp2.mixin.naturesaura;

import de.ellpeck.naturesaura.compat.jei.AnimalSpawnerCategory;
import de.ellpeck.naturesaura.recipes.AnimalSpawnerRecipe;
import de.ellpeck.naturesaura.recipes.ModRecipes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.util.RecipeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalSpawnerCategory.class)
public abstract class AnimalSpawnerCategoryMixin implements IRecipeCategory<AnimalSpawnerRecipe> {
    @Override
    public @Nullable ResourceLocation getRegistryName(@NotNull AnimalSpawnerRecipe recipe) {
        return RecipeUtil.getRecipeId(ModRecipes.ANIMAL_SPAWNER_TYPE, recipe);
    }

    @Inject(method = "draw(Lde/ellpeck/naturesaura/recipes/AnimalSpawnerRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V", at = @At("TAIL"))
    private void cdp2$draw(AnimalSpawnerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci){
        if(!CreativeDrawersProducer2.hideNeededAura){
            graphics.drawCenteredString(Minecraft.getInstance().font, Component.translatable("jei.cdp2.needed_aura",recipe.aura),34,2,0x7CFC00);
        }
    }
}
