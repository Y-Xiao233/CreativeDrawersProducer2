package net.yxiao233.cdp2.mixin.ae2cs;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.UpgradeInventories;
import io.github.lounode.ae2cs.common.block.entity.CrystalGrowthChamberBlockEntity;
import io.github.lounode.ae2cs.common.init.AECSBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrystalGrowthChamberBlockEntity.class)
public abstract class CrystalGrowthChamberBlockEntityMixin {
    @Mutable
    @Shadow
    @Final
    private IUpgradeInventory upgrades;

    @Shadow protected abstract void onUpgradesChanged();

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void cdp2$initUpgrade(BlockPos pos, BlockState blockState, CallbackInfo ci){
        this.upgrades = UpgradeInventories.forMachine(AECSBlocks.CRYSTAL_GROWTH_CHAMBER_BLOCK, 8, this::onUpgradesChanged);
    }
}
