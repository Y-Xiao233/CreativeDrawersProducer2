package net.yxiao233.cdp2.common.registry;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.util.CodecHelper;

public class CDPAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CreativeDrawersProducer2.MODID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<CompoundTag>> PLAYER_EXTRA_DATA = ATTACHMENT_TYPES.register("player_extra_data", () -> AttachmentType.<CompoundTag>builder(() -> new CompoundTag()).serialize(CompoundTag.CODEC).sync(CodecHelper.fromCodec(CompoundTag.CODEC)).copyOnDeath().build());
}
