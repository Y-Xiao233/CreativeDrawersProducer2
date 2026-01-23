package net.yxiao233.cdp2.api.block.entity;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.syncdata.holder.blockentity.ISyncPersistRPCBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.block.CDPMachineEntityBlock;
import net.yxiao233.cdp2.api.block.IRightClickedHandler;

public abstract class CDPMachineBlockEntity extends CDPCapabilitiesBlockEntity implements BlockUIMenuType.BlockUI, ISyncPersistRPCBlockEntity, ITickableBlockEntity, IRightClickedHandler {
    public CDPMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void onRightClicked(LivingEntity entity, BlockEntity blockEntity) {
        if(getLevel() != null && !getLevel().isClientSide()){
            if(entity instanceof Player player && getBlockState().getBlock() instanceof CDPMachineEntityBlock<?> machineBlock){
                machineBlock.openMenu(getLevel(),player,getBlockPos());
            }
        }
    }
}
