package net.yxiao233.cdp2.common.recipe;

import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.recipe.BaseRecipe;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.VoidSieveBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoidSieveRecipe extends BaseRecipe {
    public static final MapCodec<VoidSieveRecipe> CODEC = RecordCodecBuilder.mapCodec(in ->{
        return in.group(ChanceIngredient.CODEC.fieldOf("input").forGetter(recipe ->{
            return recipe.input;
        }), ChanceItemStack.CODEC.listOf(0,9).fieldOf("output").forGetter(recipe ->{
            return recipe.outputs;
        })).apply(in,VoidSieveRecipe::new);
    });
    public ChanceIngredient input;
    public List<ChanceItemStack> outputs;

    public VoidSieveRecipe(ChanceIngredient input, List<ChanceItemStack> outputs){
        this.input = input;
        this.outputs = outputs;
    }

    public static void createRecipe(RecipeOutput recipeOutput, String name, VoidSieveRecipe recipe) {
        createRecipe(recipeOutput,generateRL(name),recipe);
    }

    public static void createRecipe(RecipeOutput recipeOutput, ResourceLocation location, VoidSieveRecipe recipe) {
        AdvancementHolder advancementHolder = recipeOutput.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(AdvancementRewards.Builder.recipe(location)).requirements(AdvancementRequirements.Strategy.OR).build(location);
        recipeOutput.accept(location, recipe, advancementHolder);
    }

    public static ResourceLocation generateRL(String key) {
        return CreativeDrawersProducer2.makeId("void_sieve/" + key);
    }

    public boolean matches(SidedInventoryComponent<VoidSieveBlockEntity> input, SidedInventoryComponent<VoidSieveBlockEntity> output){
        if(input != null && output != null){
            return this.input.sizedIngredient().test(input.getStackInSlot(0));
        }
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CDPRecipe.VOID_SIEVE.asUnknownSerializer();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CDPRecipe.VOID_SIEVE.asUnknownType();
    }
}
