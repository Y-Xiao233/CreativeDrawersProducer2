package net.yxiao233.cdp2.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.ItemStack;

public record ChanceItemStack(ItemStack item, double chance) {
    @HideFromJS
    public static final Codec<ChanceItemStack> CODEC = RecordCodecBuilder.create(builder ->{
        return builder.group(
                ItemStack.CODEC.fieldOf("item").forGetter(ChanceItemStack::item),
                Codec.DOUBLE.optionalFieldOf("chance",1d).xmap(to -> to < 0d ? 1d : to, from -> from).forGetter(ChanceItemStack::chance)
        ).apply(builder, ChanceItemStack::new);
    });

    @Info("ItemStack stack, double chance")
    public static ChanceItemStack of(ItemStack item, double chance){
        return new ChanceItemStack(item,chance);
    }
    @Info("ItemStack stack, default chance = 1")
    public static ChanceItemStack of(ItemStack item){
        return new ChanceItemStack(item,1);
    }
}
