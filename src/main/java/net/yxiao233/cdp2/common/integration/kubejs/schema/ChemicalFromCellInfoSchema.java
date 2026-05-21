package net.yxiao233.cdp2.common.integration.kubejs.schema;

import dev.latvian.mods.kubejs.mekanism.recipe.component.ChemicalStackRecipeComponent;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemStackComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.world.item.ItemStack;

public interface ChemicalFromCellInfoSchema {
    RecipeKey<ItemStack> CELL = ItemStackComponent.ITEM_STACK.inputKey("cell");
    RecipeKey<ChemicalStack> CHEMICAL = ChemicalStackRecipeComponent.CHEMICAL_STACK.outputKey("chemical");
    RecipeSchema SCHEMA = new RecipeSchema(CELL,CHEMICAL);
}
