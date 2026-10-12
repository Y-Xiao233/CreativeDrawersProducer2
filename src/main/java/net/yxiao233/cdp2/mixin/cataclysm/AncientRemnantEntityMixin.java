package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmBosses;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The ancient remnant awakens when a player uses {@code cataclysm:necklace_of_the_desert} on it. This replaces that
 * hardcoded item with the one configured through kubejs.
 *
 * <p>As long as it is not awake it must not run the defeat logic either (loot, boss respawner), which some weapons
 * trigger on their own.</p>
 */
@Mixin(Ancient_Remnant_Entity.class)
public abstract class AncientRemnantEntityMixin {
    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/registries/DeferredItem;get()Ljava/lang/Object;"
            )
    )
    private Object cdp2$activationItem(Object original){
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.ANCIENT_REMNANT);
        return configured == null ? original : configured;
    }

    @Inject(
            method = "AfterDefeatBoss",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$noDefeatRewardsWhileAsleep(LivingEntity defeated, CallbackInfo ci){
        if(CDPCataclysmBosses.isAsleep((LivingEntity)(Object)this)){
            ci.cancel();
        }
    }
}
