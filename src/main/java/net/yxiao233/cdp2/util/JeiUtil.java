package net.yxiao233.cdp2.util;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.world.item.ItemStack;
import net.yxiao233.cdp2.integration.jei.CDPJeiPlugin;

import java.util.Optional;

public class JeiUtil {
    @SuppressWarnings("removal")
    public static void openRecipeGui(RecipeIngredientRole role, ItemStack stack){
        IJeiRuntime runtime = CDPJeiPlugin.getRuntime();
        Optional<ITypedIngredient<ItemStack>> typedOpt = runtime.getIngredientManager().createTypedIngredient(VanillaTypes.ITEM_STACK, stack);
        typedOpt.ifPresent(ingredient -> runtime.getRecipesGui().show(runtime.getJeiHelpers().getFocusFactory().createFocus(role, typedOpt.get())));
    }
}
