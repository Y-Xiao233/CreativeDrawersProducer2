package net.yxiao233.cdp2.api.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class CDPCapabilitiesBlockEntity extends CDPBaseBlockEntity implements ICapabilitiesBlockEntity {
    public CDPCapabilitiesBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        if(getCapabilityMap() != null){
            getCapabilityMap().saveAll(tag,registries);
        }
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        if(getCapabilityMap() != null){
            getCapabilityMap().loadAll(tag,registries);
        }
    }
}
