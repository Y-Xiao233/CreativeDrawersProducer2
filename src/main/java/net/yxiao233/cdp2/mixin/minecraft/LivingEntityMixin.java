package net.yxiao233.cdp2.mixin.minecraft;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * An asleep netherite monstrosity must not be damageable, otherwise the player could simply kill it before waking it
 * up with the configured item ({@code NetheriteMonstrosityEntityMixin} handles that activation).
 *
 * <p>The entity itself does not override {@code hurt}, so the check is injected into the shared base class and guarded
 * with an instanceof check.</p>
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
            method = "hurt",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$asleepBossIsInvulnerable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if(((Object)this) instanceof Netherite_Monstrosity_Entity monstrosity && !monstrosity.getIsAwaken()){
            cir.setReturnValue(false);
        }
    }
}
