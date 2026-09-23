package net.yxiao233.cdp2.common.integration.ae2.key;

import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyType;
import com.mojang.serialization.MapCodec;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ForbiddenEssenceKeyType extends AEKeyType {
    public static final ForbiddenEssenceKeyType INSTANCE = new ForbiddenEssenceKeyType();
    public ForbiddenEssenceKeyType() {
        super(ForbiddenEssenceKey.TYPE_ID, ForbiddenEssenceKey.class, Component.translatable("key_type.cdp2.forbidden_essence"));
    }

    @Override
    public MapCodec<? extends AEKey> codec() {
        return ForbiddenEssenceKey.MAP_CODEC;
    }
    public int getAmountPerByte() {
        return 1;
    }

    public int getAmountPerOperation() {
        return 1;
    }

    public int getAmountPerUnit() {
        return 1;
    }

    @Override
    public @Nullable AEKey readFromPacket(RegistryFriendlyByteBuf data) {
        return ForbiddenEssenceKey.of(data.readEnum(EssenceType.class));
    }
}
