package net.yxiao233.cdp2.common.integration.jei;

import com.jerry.meklg.common.registries.LargeGeneratorBlocks;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import cy.jdkdigital.productivebees.compat.jei.ProductiveBeesJeiPlugin;
import dev.shadowsoffire.apothic_enchanting.compat.InfusionRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.jei.CDPBaseCategory;
import net.yxiao233.cdp2.api.jei.CDPJeiCategory;
import net.yxiao233.cdp2.common.integration.jei.converter.ForbiddenEssenceConverter;
import net.yxiao233.cdp2.common.integration.jei.converter.CDPIngredientTypes;
import net.yxiao233.cdp2.common.integration.jei.converter.ForbiddenEssenceStack;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.integration.botanypot.BotanyPotJei;
import net.yxiao233.industrialforegoingextra.api.jei.AbstractJEICategory;
import net.yxiao233.industrialforegoingextra.util.AnnotationUtil;
import net.yxiao233.industrialforegoingextra.util.JeiHelper;
import org.jetbrains.annotations.NotNull;
import tamaized.ae2jeiintegration.api.integrations.jei.IngredientConverters;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class CDPJeiPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private final List<AbstractJEICategory<?>> categories = new ArrayList<>();
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return CreativeDrawersProducer2.makeId("jei");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IModPlugin.super.onRuntimeAvailable(jeiRuntime);
        runtime = jeiRuntime;

        JeiHelper.hideItem(jeiRuntime,CDPItem.TEST);
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
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        JeiHelper.registerRecipeCatalysts(registration,categories);
        BotanyPotJei.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(CDPBlock.VOID_CRAFTING_TABLE.asBlock(), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(CDPBlock.FLUX_INFUSION_ENCHANTMENT_FACTORY, InfusionRecipeCategory.TYPE);
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(register -> {
            if(!BuiltInRegistries.BLOCK.getKey(register.asBlock()).getPath().equals("void_matter_creative_drawer")){
                registration.addRecipeCatalyst(register.asItem(),CDPRecipeType.DRAWER_INFO);
            }
        });
        registration.addRecipeCatalyst(CDPBlock.BEE_CONVERTER, ProductiveBeesJeiPlugin.BEE_CONVERSION_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BEE_SPAWNER, ProductiveBeesJeiPlugin.BEE_SPAWNING_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BEE_FISHING_DEVICE, ProductiveBeesJeiPlugin.BEE_FISHING_TYPE);
        registration.addRecipeCatalyst(CDPBlock.BREEDING_CHAMBER, ProductiveBeesJeiPlugin.BEE_BREEDING_TYPE);
        registration.addRecipeCatalyst(LargeGeneratorBlocks.LARGE_GAS_BURNING_GENERATOR,CDPRecipeType.GAS_BURNING);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        categories.clear();
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        AnnotationUtil.getAllClasses(CDPJeiCategory.class).forEach((clazz) -> {
            if (AbstractJEICategory.class.isAssignableFrom(clazz)) {
                try {
                    AbstractJEICategory<?> category = (CDPBaseCategory<?>)clazz.getConstructor(IGuiHelper.class).newInstance(guiHelper);
                    categories.add(category);
                    registration.addRecipeCategories(category);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        JeiHelper.registerRecipes(registration,categories);
    }
}
