package net.yxiao233.cdp2.common.integration.jei;

import com.hrznstudio.titanium.util.RecipeUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import net.yxiao233.cdp2.common.integration.botanypot.BotanyPotJei;
import net.yxiao233.cdp2.common.integration.jei.category.VoidSieveCategory;
import org.jetbrains.annotations.NotNull;

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
    }

    public static IJeiRuntime getRuntime(){
        return runtime;
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper =  registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new VoidSieveCategory(guiHelper));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Level level = Minecraft.getInstance().level;

        registration.addRecipes(CDPRecipeType.VOID_SIEVE, RecipeUtil.getRecipes(level,(RecipeType<VoidSieveRecipe>) CDPRecipe.VOID_SIEVE_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
        BotanyPotJei.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(CDPBlock.VOID_CRAFTING_TABLE.asBlock(), RecipeTypes.CRAFTING);
        registration.addRecipeCatalyst(CDPBlock.VOID_SIEVE,CDPRecipeType.VOID_SIEVE);
    }
}
