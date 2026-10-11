package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The vanilla "do nothing" goal of the netherite monstrosity stops (and therefore awakens the boss) as soon as a
 * player stands close enough in its line of sight. An asleep monstrosity has to stay completely idle instead, the
 * configured item is the only thing that may wake it up.
 */
@Mixin(targets = "com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity$NMDoNothingGoal")
public abstract class NetheriteMonstrosityDoNothingGoalMixin {
    @Shadow
    @Final
    public Netherite_Monstrosity_Entity this$0;

    @Inject(
            method = "canUse",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$stayIdleUntilAwoken(CallbackInfoReturnable<Boolean> cir){
        if(!this.this$0.getIsAwaken()){
            cir.setReturnValue(true);
        }
    }
}
