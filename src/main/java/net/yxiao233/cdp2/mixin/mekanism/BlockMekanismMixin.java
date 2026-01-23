package net.yxiao233.cdp2.mixin.mekanism;

import mekanism.api.Upgrade;
import mekanism.common.block.BlockMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.common.block.entity.UpgradeStationBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockMekanism.class)
public abstract class BlockMekanismMixin extends Block {
    public BlockMekanismMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        BlockEntity blockEntity = WorldUtils.getTileEntity(level, pos);
        if(blockEntity instanceof IUpgradeTile tile){
            for(Upgrade upgrade : Upgrade.values()){
                if(tile.supportsUpgrade(upgrade)){
                    TileComponentUpgrade component = tile.getComponent();
                    component.removeUpgrade(upgrade,true);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        UpgradeStationBlockEntity.entries.forEach((stationPos,station)-> {
            if (station.getBoundary().contains(pos.getX(),pos.getY(),pos.getZ())) {
                station.markToUpdate(level);
            }
        });
    }
}
