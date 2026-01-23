package net.yxiao233.cdp2.api.block;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class CDPMachineEntityBlock<T extends BlockEntity & BlockUIMenuType.BlockUI> extends CDPTickableEntityBlock implements BlockUIMenuType.BlockUI {
    public CDPMachineEntityBlock(Properties properties) {
        super(properties);
    }

    public CDPMachineEntityBlock() {
        super();
    }
    public void openMenu(Level level, Player player, BlockPos pos){
        if(!level.isClientSide() && player instanceof ServerPlayer serverPlayer){
            BlockUIMenuType.openUI(serverPlayer,pos);
        }
    }

    @Override
    public ModularUI createUI(BlockUIMenuType.BlockUIHolder holder) {
        if(holder.player.level().getBlockEntity(holder.pos) instanceof BlockUIMenuType.BlockUI entity){
            return entity.createUI(holder);
        }
        return ModularUI.of(UI.of(),holder.player);
    }
}
