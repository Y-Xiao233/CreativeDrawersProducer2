package net.yxiao233.cdp2.mixin.forbiddenarcanus;

import com.stal111.forbidden_arcanus.common.block.entity.forge.ForgeDataCache;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssencesDefinition;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.RitualManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fixes Forbidden Arcanus client-side crashes.
 *
 * <p>{@link RitualManager#onDataChanged} fails the active ritual when the currently cached ingredients no longer
 * satisfy it. The failure path ends in {@code reset()} which sends a tracking-chunk packet using {@code level}/{@code pos}
 * and clears pedestals through the level. On the client those fields are never initialised (only the server calls
 * {@code setup}), causing a {@code NullPointerException} while a block entity update is being handled.</p>
 *
 * <p>{@link RitualManager#load(CompoundTag, HolderLookup.Provider)} decodes the active ritual through
 * {@code Ritual.CODEC}. The Forbidden Arcanus JS mod replaces that codec with one that resolves the ritual via
 * {@code ServerLifecycleHooks.getCurrentServer().getRecipeManager()}, which throws a {@code NullPointerException}
 * when there is no current server (a remote-client connection). We cancel the load in exactly that case; the server
 * (including the singleplayer integrated server) still restores the active ritual, so crafting survives reloads.</p>
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

    @Inject(method = "load", at = @At("HEAD"), cancellable = true)
    private void cdp2$skipClientLoad(CompoundTag tag, HolderLookup.Provider lookupProvider, CallbackInfo ci) {
        // Ritual.CODEC (as replaced by forbidden_arcanus_js) needs a running server. Only skip when there is none,
        // so the dedicated/integrated server still restores active rituals on chunk (re)load.
        if (ServerLifecycleHooks.getCurrentServer() == null) {
            ci.cancel();
        }
    }
}
