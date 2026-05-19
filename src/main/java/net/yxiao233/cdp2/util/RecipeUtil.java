package net.yxiao233.cdp2.util;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.pedroksl.advanced_ae.recipes.ReactionChamberRecipe;
import org.apache.logging.log4j.util.Cast;

import java.util.List;

public class RecipeUtil {
    public static <T extends Recipe<?>> ResourceLocation getRecipeId(RecipeType<T> type, T recipe){
        Level level = Minecraft.getInstance().level;
        if(level != null){
            List<RecipeHolder<Recipe<RecipeInput>>> recipes = level.getRecipeManager().getAllRecipesFor(Cast.cast(type));
            for (RecipeHolder<Recipe<RecipeInput>> recipeRecipeHolder : recipes) {
                if (recipeRecipeHolder.value().equals(Cast.cast(recipe))) {
                    return recipeRecipeHolder.id();
                }
            }
        }
        return null;
    }
}
