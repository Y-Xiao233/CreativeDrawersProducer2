package net.yxiao233.cdp2.api.block.property;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public interface RotationHandler {
    DirectionProperty FACING_ALL = DirectionProperty.create("facing", Direction.values());
    DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("subfacing", Direction.Plane.HORIZONTAL);
    BlockState getStateForPlacement(Block block, BlockPlaceContext context);
}
