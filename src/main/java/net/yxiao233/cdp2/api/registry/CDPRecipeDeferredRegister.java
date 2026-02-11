package net.yxiao233.cdp2.api.registry;

import com.hrznstudio.titanium.recipe.serializer.CodecRecipeSerializer;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public class CDPRecipeDeferredRegister<T extends Recipe<?>> {
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> serializer;
    private final DeferredHolder<RecipeType<?>, RecipeType<?>> type;
    private CDPRecipeDeferredRegister(DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> serializer, DeferredHolder<RecipeType<?>, RecipeType<?>> type){
        this.serializer = serializer;
        this.type = type;
    }

    public static <T extends Recipe<?>> CDPRecipeDeferredRegister<T> codecRecipe(String name, Class<T> clazz, MapCodec<T> codec){
        DeferredHolder<RecipeType<?>, RecipeType<?>> type = CDPRecipe.RECIPE_TYPES.register(name,() -> RecipeType.simple(CreativeDrawersProducer2.makeId(name)));
        DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> serializer = CDPRecipe.RECIPE_SERIALIZERS.register(name, () -> new CodecRecipeSerializer<>(clazz,type,codec));
        return new CDPRecipeDeferredRegister<>(serializer,type);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> CDPRecipeDeferredRegister<T> codecRecipe(String name, Class<T> clazz){
        MapCodec<T> codec = null;
        try{
            for (Field field : clazz.getFields()) {
                if(Modifier.isStatic(field.getModifiers()) && field.getName().equalsIgnoreCase("codec")){
                    Object o = field.get(null);
                    Class<?> mapCodecClass = MapCodec.class;
                    if(mapCodecClass.isInstance(o)){
                        codec = (MapCodec<T>) mapCodecClass.cast(o);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return codecRecipe(name,clazz,codec);
    }

    public DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> getSerializer() {
        return serializer;
    }

    public RecipeSerializer<T> asSerializer(){
        return serializer.get();
    }

    public RecipeSerializer<?> asUnknownSerializer(){
        return serializer.get();
    }

    public DeferredHolder<RecipeType<?>, RecipeType<?>> getType() {
        return type;
    }

    public RecipeType<?> asUnknownType(){
        return type.get();
    }

    @SuppressWarnings("unchecked")
    public RecipeType<T> asType(){
        return (RecipeType<T>) type.get();
    }
}
