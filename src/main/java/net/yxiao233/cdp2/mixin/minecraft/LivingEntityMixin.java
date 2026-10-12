package net.yxiao233.cdp2.mixin.minecraft;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmBosses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * An asleep Cataclysm boss must not be damageable at all, otherwise a player could simply kill it before waking it up
 * with the configured item ({@code NetheriteMonstrosityEntityMixin}, {@code TheHarbingerEntityMixin} and
 * {@code AncientRemnantEntityMixin} handle the activation).
 *
 * <p>Every damage entry point has to be covered because weapons of other mods use different ones: the ae2lt railgun
 * clears {@code invulnerableTime} and calls {@code hurt} for its normal damage, but executes its victim through
 * {@code die} when the modules are overloaded, which would otherwise one shot an asleep boss.</p>
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossIsInvulnerable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(CDPCataclysmBosses.isAsleep((LivingEntity)(Object)this)){
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "actuallyHurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossTakesNoDamage(DamageSource source, float amount, CallbackInfo ci){
        if(CDPCataclysmBosses.isAsleep((LivingEntity)(Object)this)){
            ci.cancel();
        }
    }

    /** Weapons that kill their target directly must not be able to execute an asleep boss */
    @Inject(
            method = "die",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossCannotBeExecuted(DamageSource source, CallbackInfo ci){
        LivingEntity self = (LivingEntity)(Object)this;
        if(CDPCataclysmBosses.isAsleep(self)){
            self.setHealth(self.getMaxHealth());
            ci.cancel();
        }
    }

    /** Anything that writes the health directly is blocked as well, but only when it would lower it */
    @Inject(
            method = "setHealth",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossKeepsItsHealth(float health, CallbackInfo ci){
        LivingEntity self = (LivingEntity)(Object)this;
        if(health < self.getHealth() && CDPCataclysmBosses.isAsleep(self)){
            ci.cancel();
        }
    }

    /** Loot must never drop from an asleep boss, even when some weapon runs its own death handling around it */
    @Inject(
            method = "dropAllDeathLoot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossDropsNothing(ServerLevel level, DamageSource source, CallbackInfo ci){
        if(CDPCataclysmBosses.isAsleep((LivingEntity)(Object)this)){
            ci.cancel();
        }
    }
}
