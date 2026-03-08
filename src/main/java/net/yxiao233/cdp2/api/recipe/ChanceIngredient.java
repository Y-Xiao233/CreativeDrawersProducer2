package net.yxiao233.cdp2.api.recipe;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.yxiao233.cdp2.util.ParsedItem;

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

    @SuppressWarnings("unchecked")
    @Info("Object item, int count, double chance, Supported Types:[ItemStack, Item, SizedIngredient, TagKey<Item>, ItemLike, String]")
    public static ChanceIngredient of(Object obj, int count, double chance){
        return switch (obj){
            case ItemStack stack -> ChanceIngredient.of(stack.getItem(),count,chance);
            case Item item -> ChanceIngredient.of(item,count,chance);
            case ChanceIngredient chanceIngredient -> chanceIngredient;
            case TagKey<?> tag when tag.isFor(Registries.ITEM.registryKey()) -> ChanceIngredient.of((TagKey<Item>) tag,count,chance);
            case ItemLike itemLike -> ChanceIngredient.of(itemLike,count,chance);
            case String s -> ChanceIngredient.of(parse(s).itemStack().getItem(),count,chance);
            default -> throw new IllegalArgumentException("Invalid argument: " + String.valueOf(obj));
        };
    }

    @SuppressWarnings("unchecked")
    @Info("Object item, default count = stack count/1, double chance, Supported Types:[ItemStack, Item, SizedIngredient, TagKey<Item>, ItemLike, String]")
    public static ChanceIngredient of(Object obj, double chance){
        return switch (obj){
            case ItemStack stack -> ChanceIngredient.of(stack.getItem(),stack.getCount(),chance);
            case Item item -> ChanceIngredient.of(item,1,chance);
            case ChanceIngredient chanceIngredient -> chanceIngredient;
            case TagKey<?> tag when tag.isFor(Registries.ITEM.registryKey()) -> ChanceIngredient.of((TagKey<Item>) tag,1,chance);
            case ItemLike itemLike -> ChanceIngredient.of(itemLike,1,chance);
            case String s -> ChanceIngredient.of(parse(s).itemStack().getItem(),parse(s).itemStack().getCount(),chance);
            default -> throw new IllegalArgumentException("Invalid argument: " + String.valueOf(obj));
        };
    }

    @Info("Object item, default count = stack count/1, default chance = 1, Supported Types:[ItemStack, Item, SizedIngredient, TagKey<Item>, ItemLike, String]")
    public static ChanceIngredient of(Object obj){
        return of(obj,1);
    }

    @HideFromJS
    public static ChanceIngredient of(ItemLike item, int count, double chance){
        return new ChanceIngredient(SizedIngredient.of(item,count),chance);
    }

    @HideFromJS
    public static ChanceIngredient of(ItemLike item, int count){
        return of(item,count,1);
    }

    @HideFromJS
    public static ChanceIngredient of(ItemLike item){
        return of(item,1,1);
    }

    @HideFromJS
    public static ChanceIngredient of(TagKey<Item> tag, int count, double chance){
        return new ChanceIngredient(SizedIngredient.of(tag,count),chance);
    }
    @HideFromJS
    public static ChanceIngredient of(TagKey<Item> tag, int count){
        return of(tag,count,1);
    }
    @HideFromJS
    public static ChanceIngredient of(TagKey<Item> tag){
        return of(tag,1,1);
    }

    @HideFromJS
    static ParsedItem parse(String s, boolean single) {
        try {
            return ParsedItem.read(new StringReader(s), single);
        } catch (CommandSyntaxException var3) {
            throw new RuntimeException(var3);
        }
    }

    @HideFromJS
    static ParsedItem parse(String s) {
        return parse(s, false);
    }
}
