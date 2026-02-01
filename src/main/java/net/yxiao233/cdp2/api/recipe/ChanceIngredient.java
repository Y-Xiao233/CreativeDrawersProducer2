package net.yxiao233.cdp2.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record ChanceIngredient(SizedIngredient sizedIngredient, float chance) {
    public static final Codec<ChanceIngredient> CODEC = Codec.lazyInitialized(() -> {
        return RecordCodecBuilder.create(builder ->{
            return builder.group(
                    SizedIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(ChanceIngredient::sizedIngredient),
                    Codec.FLOAT.optionalFieldOf("chance",1f).xmap(to -> to < 0f ? 1f : to, from -> from).forGetter(ChanceIngredient::chance)
            ).apply(builder, ChanceIngredient::new);
        });
    });

    public static ChanceIngredient of(ItemLike item, int count, float chance){
        return new ChanceIngredient(SizedIngredient.of(item,count),chance);
    }

    public static ChanceIngredient of(ItemLike item, int count){
        return of(item,count,1);
    }

    public static ChanceIngredient of(ItemLike item){
        return of(item,1,1);
    }

    public static ChanceIngredient of(TagKey<Item> tag, int count, float chance){
        return new ChanceIngredient(SizedIngredient.of(tag,count),chance);
    }
    public static ChanceIngredient of(TagKey<Item> tag, int count){
        return of(tag,count,1);
    }
    public static ChanceIngredient of(TagKey<Item> tag){
        return of(tag,1,1);
    }
}
