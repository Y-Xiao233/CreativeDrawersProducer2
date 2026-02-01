package net.yxiao233.cdp2.api.block.entity;

import com.buuz135.industrial.block.tile.IndustrialMachineTile;
import com.hrznstudio.titanium.module.BlockWithTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class RecipeTile<T extends RecipeTile<T>> extends IndustrialMachineTile<T> {

    public RecipeTile(BlockWithTile basicTileBlock, BlockPos blockPos, BlockState blockState) {
        super(basicTileBlock, blockPos, blockState);
    }

    public abstract void checkForRecipe();

    @Override
    public void setLevel(@NotNull Level level) {
        super.setLevel(level);
        checkForRecipe();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }
}
