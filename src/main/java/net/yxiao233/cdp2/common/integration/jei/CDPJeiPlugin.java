package net.yxiao233.cdp2.common.integration.jei;

import com.hrznstudio.titanium.util.RecipeUtil;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import cy.jdkdigital.productivebees.compat.jei.ProductiveBeesJeiPlugin;
import dev.shadowsoffire.apothic_enchanting.compat.InfusionRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.common.integration.jei.category.ChemicalFromCellInfoCategory;
import net.yxiao233.cdp2.common.integration.jei.category.CreativeDrawerInfoCategory;
import net.yxiao233.cdp2.common.integration.jei.converter.ForbiddenEssenceConverter;
import net.yxiao233.cdp2.common.integration.jei.converter.CDPIngredientTypes;
import net.yxiao233.cdp2.common.integration.jei.converter.ForbiddenEssenceStack;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import net.yxiao233.cdp2.common.integration.botanypot.BotanyPotJei;
import net.yxiao233.cdp2.common.integration.jei.category.VoidSieveCategory;
import org.jetbrains.annotations.NotNull;
import tamaized.ae2jeiintegration.api.integrations.jei.IngredientConverters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JeiPlugin
public class CDPJeiPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return CreativeDrawersProducer2.makeId("jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
        runtime = jeiRuntime;

        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, Collections.singletonList(CDPItem.TEST.asStack()));
        IngredientConverters.register(new ForbiddenEssenceConverter(CDPIngredientTypes.ESSENCE_TYPE));
    }

    public static IJeiRuntime getRuntime(){
        return runtime;
    }

    @Override
    @SuppressWarnings("removal")
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(CDPIngredientTypes.ESSENCE_TYPE,List.of(
                new ForbiddenEssenceStack(EssenceType.AUREAL,1),
                new ForbiddenEssenceStack(EssenceType.BLOOD,1),
                new ForbiddenEssenceStack(EssenceType.SOULS,1),
                new ForbiddenEssenceStack(EssenceType.EXPERIENCE,1)
        ),new CDPIngredientTypes.EssenceStackHelper(),new CDPIngredientTypes.EssenceStackRenderer());
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper =  registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new VoidSieveCategory(guiHelper));
        registration.addRecipeCategories(new CreativeDrawerInfoCategory(guiHelper));
        registration.addRecipeCategories(new ChemicalFromCellInfoCategory(guiHelper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;
        if(level != null){
            registration.addRecipes(CDPRecipeType.VOID_SIEVE, RecipeUtil.getRecipes(level,CDPRecipe.VOID_SIEVE.asType()));
            registration.addRecipes(CDPRecipeType.CHEMICAL_FROM_CELL_INFO, RecipeUtil.getRecipes(level,CDPRecipe.CHEMICAL_FROM_CELL_INFO.asType()));
        }
        addDrawerInfoRecipe(registration);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        BotanyPotJei.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(CDPBlock.VOID_CRAFTING_TABLE.asBlock(), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(CDPBlock.VOID_SIEVE,CDPRecipeType.VOID_SIEVE);
        registration.addRecipeCatalyst(CDPBlock.FLUX_INFUSION_ENCHANTMENT_FACTORY, InfusionRecipeCategory.TYPE);
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(register -> registration.addRecipeCatalyst(register.asItem(),CDPRecipeType.DRAWER_INFO));
        registration.addRecipeCatalyst(CDPBlock.BEE_CONVERTER, ProductiveBeesJeiPlugin.BEE_CONVERSION_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BEE_SPAWNER, ProductiveBeesJeiPlugin.BEE_SPAWNING_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BEE_FISHING_DEVICE, ProductiveBeesJeiPlugin.BEE_FISHING_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BREEDING_CHAMBER, ProductiveBeesJeiPlugin.BEE_BREEDING_TYPE);
    }

    private void addDrawerInfoRecipe(IRecipeRegistration registration){
        List<CreativeDrawerInfo> recipes = new ArrayList<>();
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(register -> {
            ItemStack drawer = register.asStack();
            ItemStack infinityItem =  ((CreativeDrawerBlock) register.asBlock()).getInfinityItem().get();
            recipes.add(new CreativeDrawerInfo(drawer,infinityItem));
        });

        registration.addRecipes(CDPRecipeType.DRAWER_INFO,recipes);
    }
}
