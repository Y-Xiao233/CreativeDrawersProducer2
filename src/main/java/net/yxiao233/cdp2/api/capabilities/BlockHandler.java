package net.yxiao233.cdp2.api.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;

public interface BlockHandler<T> {
    T getHandler();
    void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries);
    void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries);
    BlockCapability<T, Direction> getCapability();
}
