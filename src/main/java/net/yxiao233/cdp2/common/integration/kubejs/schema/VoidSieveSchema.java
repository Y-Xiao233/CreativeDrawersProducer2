package net.yxiao233.cdp2.common.integration.kubejs.schema;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;
import net.yxiao233.cdp2.common.integration.kubejs.component.ChanceIngredientComponent;
import net.yxiao233.cdp2.common.integration.kubejs.component.ChanceItemStackComponent;

import java.util.List;

public interface VoidSieveSchema {
    RecipeKey<ChanceIngredient> INPUT = ChanceIngredientComponent.SINGLE.inputKey("input");
    RecipeKey<List<ChanceItemStack>> OUTPUTS = ChanceItemStackComponent.LIST.outputKey("output");
    RecipeSchema SCHEMA = new RecipeSchema(INPUT,OUTPUTS);
}
