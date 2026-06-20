package net.yxiao233.cdp2.mixin.ifs;

import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.common.integration.kubejs.event.BlockModifyEvent;
import net.yxiao233.ifs.common.tile.SimulatedMobDuplicatorTile;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SimulatedMobDuplicatorTile.class)
public abstract class SimulatedMobDuplicatorTileMixin {
    @Shadow
    private SidedInventoryComponent<SimulatedMobDuplicatorTile> input;

    @Shadow
    public abstract @NotNull SimulatedMobDuplicatorTile getSelf();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cdp2$onInit(BlockPos blockPos, BlockState blockState, CallbackInfo ci){
        this.input.setInputFilter((stack, integer) -> {
            if(stack.getItem() instanceof MobImprisonmentToolItem toolItem){
                Entity entityFromStack = toolItem.getEntityFromStack(stack, this.getSelf().getLevel(), true, true);
                if(entityFromStack != null){
                    String id = entityFromStack.getEncodeId();
                    return BlockModifyEvent.WHITE_LIST.contains(id);
                }
            }
            return false;
        });
    }
}
