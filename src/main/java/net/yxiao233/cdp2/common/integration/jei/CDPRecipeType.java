package net.yxiao233.cdp2.common.integration.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.jei.category.GasBurningCategory;
import net.yxiao233.cdp2.common.recipe.ChemicalFromCellInfo;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;

public class CDPRecipeType {
    private static final String namespace = CreativeDrawersProducer2.MODID;
    public static final RecipeType<RecipeHolder<VoidSieveRecipe>> VOID_SIEVE = creatHolderType("void_sieve");
    public static final RecipeType<CreativeDrawerInfo> DRAWER_INFO = createType("creative_drawer_info",CreativeDrawerInfo.class);
    public static final RecipeType<RecipeHolder<ChemicalFromCellInfo>> CHEMICAL_FROM_CELL_INFO = creatHolderType("chemical_from_cell_info");
    public static final RecipeType<GasBurningCategory.GasBurningRecipeWrapper> GAS_BURNING = createType("gas_burning", GasBurningCategory.GasBurningRecipeWrapper.class);
    private static <T> RecipeType<T> createType(String name, Class<? extends T> clazz){
        return RecipeType.create(CreativeDrawersProducer2.MODID,name,clazz);
    }
    private static <T extends Recipe<?>> RecipeType<RecipeHolder<T>> creatHolderType(String name){
        return RecipeType.createRecipeHolderType(CreativeDrawersProducer2.makeId(name));
    }
}
