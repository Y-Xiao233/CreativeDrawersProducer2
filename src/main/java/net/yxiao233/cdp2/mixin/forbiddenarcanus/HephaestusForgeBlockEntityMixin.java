package net.yxiao233.cdp2.mixin.forbiddenarcanus;

import com.stal111.forbidden_arcanus.common.block.HephaestusForgeBlock;
import com.stal111.forbidden_arcanus.common.block.entity.forge.HephaestusForgeBlockEntity;
import com.stal111.forbidden_arcanus.common.block.entity.forge.essence.EssenceManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prevents tick acceleration from lagging the whole world.
 *
 * <p>{@code HephaestusForgeBlockEntity.serverTick} guards its structure check with {@code gameTime % 80} and its
 * entity scan with {@code gameTime % 20}. Tick accelerators invoke the ticker many times with the <b>same</b>
 * game time, so those guards pass on every sub-tick and the expensive 9x9 structure pattern search / 5 block entity
 * scan run hundreds of times per game tick.</p>
 *
 * <p>We allow them to run at most once per game tick (per forge). The ritual itself is still driven by
 * {@code ritualManager.tick()} which is not throttled here, so acceleration still speeds up crafting.</p>
 */
@Mixin(HephaestusForgeBlockEntity.class)
public abstract class HephaestusForgeBlockEntityMixin {
    @Unique
    private static final Map<GlobalPos, Long> cdp2$lastStateTick = new ConcurrentHashMap<>();
    @Unique
    private static final Map<GlobalPos, Long> cdp2$lastEssenceTick = new ConcurrentHashMap<>();

    @Redirect(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/stal111/forbidden_arcanus/common/block/HephaestusForgeBlock;updateState(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private static void cdp2$throttleUpdateState(HephaestusForgeBlock block, BlockState state, Level level, BlockPos pos) {
        if (cdp2$allow(level, pos, cdp2$lastStateTick)) {
            block.updateState(state, level, pos);
        }
    }

    @Redirect(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/stal111/forbidden_arcanus/common/block/entity/forge/essence/EssenceManager;tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            )
    )
    private static void cdp2$throttleEssenceTick(EssenceManager manager, Level level, BlockPos pos) {
        if (cdp2$allow(level, pos, cdp2$lastEssenceTick)) {
            manager.tick(level, pos);
        }
    }

    @Unique
    private static boolean cdp2$allow(Level level, BlockPos pos, Map<GlobalPos, Long> tracker) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return true;
        }
        GlobalPos key = GlobalPos.of(serverLevel.dimension(), pos);
        long tick = serverLevel.getGameTime();
        Long last = tracker.get(key);
        if (last != null && last == tick) {
            return false;
        }
        if (tracker.size() > 1024) {
            tracker.clear();
        }
        tracker.put(key, tick);
        return true;
    }
}
