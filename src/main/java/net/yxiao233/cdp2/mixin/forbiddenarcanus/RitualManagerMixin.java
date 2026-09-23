package net.yxiao233.cdp2.mixin.forbiddenarcanus;

import com.stal111.forbidden_arcanus.common.block.entity.forge.ForgeDataCache;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssencesDefinition;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.RitualManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fixes a Forbidden Arcanus client-side crash.
 *
 * <p>{@link RitualManager#onDataChanged} fails the active ritual when the currently cached ingredients no longer
 * satisfy it. The failure path ends in {@code reset()} which sends a tracking-chunk packet using {@code level}/{@code pos}
 * and clears pedestals through the level. On the client those fields are never initialised (only the server calls
 * {@code setup}), causing a {@code NullPointerException} while a block entity update is being handled.</p>
 *
 * <p>On the client we simply refresh the cached data and the valid ritual, skipping the failure/reset logic.</p>
 */
@Mixin(RitualManager.class)
public abstract class RitualManagerMixin {
    @Shadow
    private ServerLevel level;

    @Shadow
    private ForgeDataCache dataCache;

    @Shadow
    public abstract void updateValidRitual(EssencesDefinition definition, HolderLookup.Provider lookupProvider);

    @Inject(method = "onDataChanged", at = @At("HEAD"), cancellable = true)
    private void cdp2$skipClientFailCheck(ForgeDataCache dataCache, EssencesDefinition essencesDefinition,
                                          HolderLookup.Provider lookupProvider, CallbackInfo ci) {
        if (this.level == null) {
            this.dataCache = dataCache;
            this.updateValidRitual(essencesDefinition, lookupProvider);
            ci.cancel();
        }
    }
}
