package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CDPRecipeProvider extends RecipeProvider {
    public CDPRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
//        VoidSieveRecipe.createRecipe(recipeOutput,"test",new VoidSieveRecipe(ChanceIngredient.of(ItemTags.OAK_LOGS), List.of(
//                ChanceItemStack.of(Items.DIAMOND.getDefaultInstance().copyWithCount(2),0.5f)
//        )));
    }
}
