package net.yxiao233.cdp2.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.api.recipe.BaseRecipe;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import org.jetbrains.annotations.NotNull;

public class ChemicalFromCellInfo extends BaseRecipe {
    public static final MapCodec<ChemicalFromCellInfo> CODEC = RecordCodecBuilder.mapCodec(in ->{
        return in.group(ItemStack.CODEC.fieldOf("cell").forGetter(recipe ->{
            return recipe.cell;
        }),ChemicalStack.CODEC.fieldOf("chemical").forGetter(recipe ->{
            return recipe.chemical;
        })).apply(in,ChemicalFromCellInfo::new);
    });

    public ChemicalStack chemical;
    public ItemStack cell;

    public ChemicalFromCellInfo(ItemStack cell, ChemicalStack chemical){
        this.cell = cell;
        this.chemical = chemical;
    }
    public static void createRecipe(RecipeOutput recipeOutput, ResourceLocation location, ChemicalFromCellInfo recipe) {
        AdvancementHolder advancementHolder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(AdvancementRequirements.Strategy.OR).build(location);
        recipeOutput.accept(location, recipe, advancementHolder);
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CDPRecipe.CHEMICAL_FROM_CELL_INFO.asSerializer();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CDPRecipe.CHEMICAL_FROM_CELL_INFO.asUnknownType();
    }
}
