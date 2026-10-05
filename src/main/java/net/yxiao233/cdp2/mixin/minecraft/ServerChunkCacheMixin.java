package net.yxiao233.cdp2.mixin.minecraft;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.GameRules;
import net.yxiao233.cdp2.common.registry.CDPDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Lets the unknown dimension ignore the doMobSpawning game rule.
 *
 * <p>{@code ServerChunkCache.tickChunks} reads {@code GameRules.RULE_DOMOBSPAWNING} once and skips the whole natural
 * spawning (including the custom spawners) when it is disabled. That read is the only {@code GameRules#getBoolean}
 * call of the method, so returning {@code true} for the unknown dimension makes the vanilla spawner run there as if
 * the rule was enabled. Everything else stays vanilla: mob cap, pack spawning, distance to the player, difficulty
 * (peaceful still stops monster spawning) and despawning.</p>
 */
@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {
    @Shadow
    @Final
    public ServerLevel level;

    @ModifyExpressionValue(
            method = "tickChunks",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
            )
    )
    private boolean cdp2$ignoreDoMobSpawning(boolean original){
        if(this.level.dimension().equals(CDPDimension.UNKNOWN.getLevel())){
            return true;
        }
        return original;
    }
}
