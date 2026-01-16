package net.yxiao233.cdp2.api.block.property;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import static net.yxiao233.cdp2.api.block.property.RotationHandler.FACING_ALL;
import static net.yxiao233.cdp2.api.block.property.RotationHandler.FACING_HORIZONTAL;


public enum RotationType {
    NONE((block, context) -> {
        return block.defaultBlockState();
    }, new DirectionProperty[0]),
    FOUR_WAY((block, context) -> {
        return (BlockState)block.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite());
    }, new DirectionProperty[]{FACING_HORIZONTAL}),
    SIX_WAY((block, context) -> {
        return (BlockState)block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection().getOpposite());
    }, new DirectionProperty[]{FACING_ALL}),
    TWENTY_FOUR_WAY((block, context) -> {
        return (BlockState)block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection());
    }, new DirectionProperty[]{FACING_ALL, FACING_HORIZONTAL});

    private final RotationHandler handler;
    private final DirectionProperty[] properties;

    private RotationType(RotationHandler handler, DirectionProperty... properties) {
        this.handler = handler;
        this.properties = properties;
    }

    public RotationHandler getHandler() {
        return this.handler;
    }

    public DirectionProperty[] getProperties() {
        return this.properties;
    }
}
