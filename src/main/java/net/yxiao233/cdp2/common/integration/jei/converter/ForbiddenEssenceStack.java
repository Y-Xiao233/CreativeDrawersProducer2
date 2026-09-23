package net.yxiao233.cdp2.common.integration.jei.converter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;

public record ForbiddenEssenceStack(EssenceType type, int amount){
    public static final Codec<ForbiddenEssenceStack> CODEC = RecordCodecBuilder.create(obj ->{
        return obj.group(EssenceType.CODEC.fieldOf("type").forGetter(ForbiddenEssenceStack::type))
                .and(Codec.INT.fieldOf("amount").forGetter(ForbiddenEssenceStack::amount))
                .apply(obj,ForbiddenEssenceStack::new);
    });
}
