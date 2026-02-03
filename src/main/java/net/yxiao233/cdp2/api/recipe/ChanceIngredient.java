package net.yxiao233.cdp2.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

public record ChanceIngredient(SizedIngredient sizedIngredient, double chance) {
    @HideFromJS
    public static final Codec<ChanceIngredient> CODEC = Codec.lazyInitialized(() -> {
        return RecordCodecBuilder.create(builder ->{
            return builder.group(
                    SizedIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(ChanceIngredient::sizedIngredient),
                    Codec.DOUBLE.optionalFieldOf("chance",1d).xmap(to -> to < 0d ? 1d : to, from -> from).forGetter(ChanceIngredient::chance)
            ).apply(builder, ChanceIngredient::new);
        });
    });

    @Info("ItemLike item, int count, double chance")
    public static ChanceIngredient of(ItemLike item, int count, double chance){
        return new ChanceIngredient(SizedIngredient.of(item,count),chance);
    }

    @Info("ItemLike item, int count, default chance = 1")
    public static ChanceIngredient of(ItemLike item, int count){
        return of(item,count,1);
    }

    @Info("ItemLike item, default count = 1, default chance = 1")
    public static ChanceIngredient of(ItemLike item){
        return of(item,1,1);
    }

    @Info("TagKey<Item> tag, int count, double chance")
    public static ChanceIngredient of(TagKey<Item> tag, int count, double chance){
        return new ChanceIngredient(SizedIngredient.of(tag,count),chance);
    }
    @Info("TagKey<Item> tag, int count, default chance = 1")
    public static ChanceIngredient of(TagKey<Item> tag, int count){
        return of(tag,count,1);
    }
    @Info("TagKey<Item> tag, default count = 1, default chance = 1")
    public static ChanceIngredient of(TagKey<Item> tag){
        return of(tag,1,1);
    }
}
