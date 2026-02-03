package net.yxiao233.cdp2.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.ItemStack;

public record ChanceItemStack(ItemStack item, float chance) {
    @HideFromJS
    public static final Codec<ChanceItemStack> CODEC = RecordCodecBuilder.create(builder ->{
        return builder.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ChanceItemStack::item),
                Codec.FLOAT.optionalFieldOf("chance",1f).xmap(to -> to < 0f ? 1f : to, from -> from).forGetter(ChanceItemStack::chance)
        ).apply(builder, ChanceItemStack::new);
    });

    @Info("ItemStack stack, float chance")
    public static ChanceItemStack of(ItemStack item, float chance){
        return new ChanceItemStack(item,chance);
    }
    @Info("ItemStack stack, default chance = 1")
    public static ChanceItemStack of(ItemStack item){
        return new ChanceItemStack(item,1);
    }
}
