package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public class CDPDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CreativeDrawersProducer2.MODID);
    public static final DeferredHolder<DataComponentType<?>,DataComponentType<CompoundTag>> COMPOUND_TAG = DATA_COMPONENTS.register("compound_tag",() -> DataComponentType.<CompoundTag>builder().persistent(CompoundTag.CODEC).build());
}
