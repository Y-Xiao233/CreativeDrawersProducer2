package net.yxiao233.cdp2.mixin.ae2lt;

import com.moakiee.ae2lt.blockentity.FirmamentConversionCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FirmamentConversionCoreBlockEntity.class)
public abstract class FirmamentConversionCoreBlockEntityMixin extends BlockEntity {
    @Unique
    private boolean creativeDrawersProducer2$canProcess = false;
    public FirmamentConversionCoreBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "canProcessHere", at = @At("HEAD"), cancellable = true)
    private void cdp2$canProcessHere(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(creativeDrawersProducer2$canProcess);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void cdp2$saveAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci){
        tag.putBoolean("canProcess", creativeDrawersProducer2$canProcess);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void cdp2loadAdditional(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci){
        this.creativeDrawersProducer2$canProcess = tag.getBoolean("canProcess");
    }
}
