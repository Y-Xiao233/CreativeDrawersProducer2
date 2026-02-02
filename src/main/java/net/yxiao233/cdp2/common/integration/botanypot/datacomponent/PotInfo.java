package net.yxiao233.cdp2.common.integration.botanypot.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record PotInfo(ItemStack soil, ItemStack seed) {
    public static final Codec<PotInfo> CODEC = RecordCodecBuilder.create(builder ->{
        return builder.group(
                ItemStack.OPTIONAL_CODEC.fieldOf("soil").forGetter(PotInfo::soil),
                ItemStack.OPTIONAL_CODEC.fieldOf("seed").forGetter(PotInfo::seed)
        ).apply(builder,PotInfo::new);
    });

    public static PotInfo of(ItemStack soil, ItemStack seed){
        return new PotInfo(soil, seed);
    }
}
