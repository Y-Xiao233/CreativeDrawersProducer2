package net.yxiao233.cdp2.mixin.ae2lt;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmBosses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The railgun of ae2lt (AE2 Lightning Tech Reborn) runs its whole overload execution itself when the modules are
 * full: it drops the loot and removes the target, which made an asleep Cataclysm boss look killed (loot + boss
 * respawner appeared) while the entity stayed behind.
 *
 * <p>Blocking the execution entry point keeps an asleep boss completely out of that logic. {@code @Pseudo} makes the
 * mixin optional, it is simply skipped when ae2lt is not installed.</p>
 */
@Pseudo
@Mixin(targets = "com.moakiee.ae2lt.logic.railgun.OverloadExecutionService")
public abstract class OverloadExecutionServiceMixin {
    @Inject(
            method = "onHit",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void cdp2$skipAsleepBoss(ServerLevel level, ServerPlayer player, ItemStack stack, LivingEntity target, double amount, CallbackInfo ci){
        if(CDPCataclysmBosses.isAsleep(target)){
            ci.cancel();
        }
    }
}
