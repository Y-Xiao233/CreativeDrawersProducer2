package net.yxiao233.cdp2.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record ChanceItemStack(ItemStack item, float chance) {
    public static final Codec<ChanceItemStack> CODEC = RecordCodecBuilder.create(builder ->{
        return builder.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ChanceItemStack::item),
                Codec.FLOAT.optionalFieldOf("chance",1f).xmap(to -> to < 0f ? 1f : to, from -> from).forGetter(ChanceItemStack::chance)
        ).apply(builder, ChanceItemStack::new);
    });

    public static ChanceItemStack of(ItemStack item, float chance){
        return new ChanceItemStack(item,chance);
    }
    public static ChanceItemStack of(ItemStack item){
        return new ChanceItemStack(item,1);
    }
}
