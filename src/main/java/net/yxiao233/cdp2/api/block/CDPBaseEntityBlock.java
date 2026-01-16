package net.yxiao233.cdp2.api.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.yxiao233.cdp2.api.block.entity.CDPBaseBlockEntity;
import net.yxiao233.cdp2.api.block.property.IRotatableBlock;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class CDPBaseEntityBlock extends BaseEntityBlock {
    public CDPBaseEntityBlock(Properties properties) {
        super(properties);
    }

    public CDPBaseEntityBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0,0,0,16,16,16);
    }

    @Override
    public boolean canHarvestBlock(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Player player) {
        return true;
    }

    @Override
    protected abstract @NotNull MapCodec<? extends BaseEntityBlock> codec();

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);
    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CDPBaseBlockEntity entity) {
                entity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @SuppressWarnings("deprecation")
    protected @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rot) {
        if(this instanceof IRotatableBlock rotatedBlock){
            return rotatedBlock.getRotationType().getProperties().length > 0 ? (BlockState)state.setValue(rotatedBlock.getRotationType().getProperties()[0], rot.rotate((Direction)state.getValue(rotatedBlock.getRotationType().getProperties()[0]))) : super.rotate(state, rot);
        }
        return super.rotate(state,rot);
    }

    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirrorIn) {
        if(this instanceof IRotatableBlock rotatedBlock){
            return rotatedBlock.getRotationType().getProperties().length > 0 ? state.rotate(mirrorIn.getRotation((Direction)state.getValue(rotatedBlock.getRotationType().getProperties()[0]))) : super.mirror(state, mirrorIn);
        }
        return super.mirror(state,mirrorIn);
    }
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        if(this instanceof IRotatableBlock rotatedBlock){
            return rotatedBlock.getRotationType().getHandler().getStateForPlacement(this, context);
        }
        return super.getStateForPlacement(context);
    }

    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        if(this instanceof IRotatableBlock rotatedBlock){
            if (rotatedBlock.getRotationType().getProperties() != null) {
                builder.add(rotatedBlock.getRotationType().getProperties());
            }
        }
    }
}
