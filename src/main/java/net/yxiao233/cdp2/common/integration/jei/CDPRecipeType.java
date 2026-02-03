package net.yxiao233.cdp2.common.integration.jei;

import mezz.jei.api.recipe.RecipeType;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;

public class CDPRecipeType {
    private static final String namespace = CreativeDrawersProducer2.MODID;
    public static final RecipeType<VoidSieveRecipe> VOID_SIEVE = RecipeType.create(namespace,"void_sieve", VoidSieveRecipe.class);
    public static final RecipeType<CreativeDrawerInfo> DRAWER_INFO = RecipeType.create(namespace,"creative_drawer_info", CreativeDrawerInfo.class);
}
