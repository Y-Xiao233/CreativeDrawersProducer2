package net.yxiao233.cdp2.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CodecHelper {
    public static <T> StreamCodec<RegistryFriendlyByteBuf, T> fromCodec(Codec<T> codec){
        return StreamCodec.ofMember((value, buff) -> {
            buff.writeJsonWithCodec(codec, value);
        }, (registryFriendlyByteBuf) -> {
            return registryFriendlyByteBuf.readJsonWithCodec(codec);
        });
    }
}
