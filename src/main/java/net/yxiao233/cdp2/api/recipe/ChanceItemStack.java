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
import net.minecraft.world.level.ItemLike;
import net.yxiao233.cdp2.util.ParsedItem;

public record ChanceItemStack(ItemStack item, double chance) {
    @HideFromJS
    public static final Codec<ChanceItemStack> CODEC = RecordCodecBuilder.create(builder ->{
        return builder.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ChanceItemStack::item),
                Codec.DOUBLE.optionalFieldOf("chance",1d).xmap(to -> to < 0d ? 1d : to, from -> from).forGetter(ChanceItemStack::chance)
        ).apply(builder, ChanceItemStack::new);
    });


    @Info("Object item, int count, double chance, Supported Types:[ItemStack, Item, ItemLike, String]")
    public static ChanceItemStack of(Object obj, int count, double chance){
        return switch (obj){
            case ItemStack stack -> ChanceItemStack.of(new ItemStack(stack.getItem(), count),chance);
            case Item item -> ChanceItemStack.of(new ItemStack(item,count),chance);
            case ChanceItemStack chanceItemStack -> chanceItemStack;
            case ItemLike itemLike -> ChanceItemStack.of(new ItemStack(itemLike, count),chance);
            case String s -> ChanceItemStack.of(new ItemStack(parse(s).itemStack().getItem(),count),chance);
            default -> throw new IllegalArgumentException("Invalid argument: " + String.valueOf(obj));
        };
    }

    @Info("Object item, default count = stack count/1, double chance, Supported Types:[ItemStack, Item, ItemLike, String]")
    public static ChanceItemStack of(Object obj, double chance){
        return switch (obj){
            case ItemStack stack -> ChanceItemStack.of(stack,chance);
            case Item item -> ChanceItemStack.of(new ItemStack(item,1),chance);
            case ChanceItemStack chanceItemStack -> chanceItemStack;
            case ItemLike itemLike -> ChanceItemStack.of(new ItemStack(itemLike, 1),chance);
            case String s -> ChanceItemStack.of(parse(s).itemStack(),chance);
            default -> throw new IllegalArgumentException("Invalid argument: " + String.valueOf(obj));
        };
    }

    @Info("Object item, default count = stack count/1, default chance = 1, Supported Types:[ItemStack, Item, ItemLike, String]")
    public static ChanceItemStack of(Object obj){
        return of(obj,1);
    }
    @HideFromJS
    public static ChanceItemStack of(ItemStack item, double chance){
        return new ChanceItemStack(item,chance);
    }
    @HideFromJS
    public static ChanceItemStack of(ItemStack item){
        return new ChanceItemStack(item,1);
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
