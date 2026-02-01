package net.yxiao233.cdp2.common.registry;

import com.hrznstudio.titanium.recipe.serializer.CodecRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;

public class CDPRecipe {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreativeDrawersProducer2.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE,CreativeDrawersProducer2.MODID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> VOID_SIEVE_SERIALIZER;
    public static DeferredHolder<RecipeType<?>, RecipeType<?>> VOID_SIEVE_TYPE;
    static {
        VOID_SIEVE_SERIALIZER = RECIPE_SERIALIZERS.register("void_sieve", () -> new CodecRecipeSerializer<>(VoidSieveRecipe.class,VOID_SIEVE_TYPE,VoidSieveRecipe.CODEC));
        VOID_SIEVE_TYPE = RECIPE_TYPES.register("void_sieve",() -> RecipeType.simple(CreativeDrawersProducer2.makeId("void_sieve")));
    }

    public static void init(IEventBus eventBus){
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}
