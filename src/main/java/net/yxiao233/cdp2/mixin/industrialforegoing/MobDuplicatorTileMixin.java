package net.yxiao233.cdp2.mixin.industrialforegoing;

import com.buuz135.industrial.block.agriculturehusbandry.tile.MobDuplicatorTile;
import com.buuz135.industrial.item.MobImprisonmentToolItem;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.common.integration.kubejs.event.BlockModifyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobDuplicatorTile.class)
public abstract class MobDuplicatorTileMixin {
    @Shadow
    private SidedInventoryComponent<MobDuplicatorTile> input;

    @Shadow
    public abstract ActiveTile<?> getSelf();

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
