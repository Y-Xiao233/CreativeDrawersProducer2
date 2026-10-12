package net.yxiao233.cdp2.mixin.arsnouveau;

import com.hollingsworth.arsnouveau.common.ritual.RitualMobCapture;
import net.minecraft.world.entity.Entity;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmBosses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Ars Nouveau's ritual of containment can put a mob into a mob jar, which allowed an asleep Cataclysm boss to be
 * captured and carried away before it was ever activated.
 *
 * <p>{@code canJar} decides whether the ritual may take an entity, so asleep bosses are simply rejected there. Once a
 * boss was woken up with the configured item it can be captured like any other mob.</p>
 */
@Mixin(RitualMobCapture.class)
public abstract class RitualMobCaptureMixin {
    @Inject(
            method = "canJar",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$cannotJarAsleepBoss(Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(CDPCataclysmBosses.isAsleep(entity)){
            cir.setReturnValue(false);
        }
    }
}
