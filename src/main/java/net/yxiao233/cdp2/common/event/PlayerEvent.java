package net.yxiao233.cdp2.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.block.ILeftClickHandler;
import net.yxiao233.cdp2.api.block.IRightClickedHandler;
import net.yxiao233.cdp2.api.block.property.RotationHandler;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerEvent {
    @SubscribeEvent
    public static void onLeftClicked(PlayerInteractEvent.LeftClickBlock event){
        BlockState blockState = event.getLevel().getBlockState(event.getPos());
        if(blockState.getBlock() instanceof CreativeDrawerBlock block){
            Direction blockDirection = blockState.getValue(RotationHandler.FACING_HORIZONTAL);
            BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
            if(blockEntity instanceof ILeftClickHandler handler && CreativeDrawerBlockEntity.getHitDirection(event.getLevel(),event.getEntity()) == blockDirection){
                handler.onLeftClicked(event.getEntity(),blockEntity);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClicked(PlayerInteractEvent.RightClickBlock event){
        BlockState blockState = event.getLevel().getBlockState(event.getPos());
        if(blockState.getBlock() instanceof CreativeDrawerBlock block){
            Direction blockDirection = blockState.getValue(RotationHandler.FACING_HORIZONTAL);
            BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
            if(event.getEntity().isShiftKeyDown()){
                return;
            }
            if(blockEntity instanceof IRightClickedHandler handler && CreativeDrawerBlockEntity.getHitDirection(event.getLevel(),event.getEntity()) == blockDirection){
                handler.onRightClicked(event.getEntity(),blockEntity);
                event.setCanceled(true);
            }
        }
    }
}
