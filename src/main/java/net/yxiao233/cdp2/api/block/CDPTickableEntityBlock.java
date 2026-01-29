package net.yxiao233.cdp2.api.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.block.entity.ITickableBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CDPTickableEntityBlock extends CDPBaseEntityBlock{
    public CDPTickableEntityBlock(Properties properties) {
        super(properties);
    }

    public CDPTickableEntityBlock() {
        super();
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()){
            return null;
        }

        return createTickerHelper(blockEntityType, getBlockEntityType(),(l, blockPos, blockState, blockEntity) -> blockEntity.tick(level,blockPos,blockState));
    }

    public abstract BlockEntityType<? extends ITickableBlockEntity> getBlockEntityType();
}
