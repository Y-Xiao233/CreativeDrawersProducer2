package net.yxiao233.cdp2.common.registry;

import com.hrznstudio.titanium.recipe.serializer.CodecRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.registry.CDPRecipeDeferredRegister;
import net.yxiao233.cdp2.common.recipe.CreativeDrawerInfo;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;

public class CDPRecipe {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreativeDrawersProducer2.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE,CreativeDrawersProducer2.MODID);

    public static final CDPRecipeDeferredRegister<VoidSieveRecipe> VOID_SIEVE = CDPRecipeDeferredRegister.codecRecipe("void_sieve", VoidSieveRecipe.class);
    public static final CDPRecipeDeferredRegister<CreativeDrawerInfo> CREATIVE_DRAWER_INFO = CDPRecipeDeferredRegister.codecRecipe("creative_drawer_info", CreativeDrawerInfo.class);

    public static void init(IEventBus eventBus){
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
