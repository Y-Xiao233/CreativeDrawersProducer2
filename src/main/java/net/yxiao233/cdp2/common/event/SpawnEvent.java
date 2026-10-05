package net.yxiao233.cdp2.common.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPDimension;
import net.yxiao233.cdp2.common.registry.CDPSpawns;

/**
 * Spawn rules of the unknown dimension's mobs (see {@link CDPSpawns}):
 * <ul>
 *     <li>the light level is ignored</li>
 *     <li>they only spawn underground, any cave below the surface is valid</li>
 *     <li>a cave only holds a few of them, see {@link #MAX_URCHINKIN_NEARBY}</li>
 * </ul>
 * The dimension also ignores the doMobSpawning game rule, but that is done in
 * {@link net.yxiao233.cdp2.mixin.minecraft.ServerChunkCacheMixin}, so the vanilla spawner runs there unchanged.
 * Everything else (mob cap, pack spawning, distance to the player, despawning) is vanilla hostile mob behaviour.
 */
@SuppressWarnings("removal")
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.GAME)
public class SpawnEvent {
    /**
     * The vanilla monster cap (70 per player) is shared by every monster type, but this dimension has a single one, so
     * it would fill the whole cap on its own. This adds a local cap on top of it: once this many urchinkin are within
     * {@link #CROWD_RADIUS} blocks of the spawn position, the cave is considered full.
     */
    private static final int MAX_URCHINKIN_NEARBY = 4;
    private static final double CROWD_RADIUS = 48.0;

    @SubscribeEvent
    public static void onSpawnPlacementCheck(MobSpawnEvent.SpawnPlacementCheck event){
        if(event.getSpawnType() != MobSpawnType.NATURAL){
            return;
        }
        if(event.getEntityType() != CDPSpawns.urchinkin()){
            return;
        }
        if(!isUnknownDimension(event.getLevel().getLevel())){
            return;
        }
        //Ignore the light level (and any other spawn placement rule the mob may get in the future)
        event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.SUCCEED);
    }

    @SubscribeEvent
    public static void onPositionCheck(MobSpawnEvent.PositionCheck event){
        if(event.getSpawnType() != MobSpawnType.NATURAL){
            return;
        }
        if(event.getEntity().getType() != CDPSpawns.urchinkin()){
            return;
        }
        ServerLevel level = event.getLevel().getLevel();
        if(!isUnknownDimension(level)){
            return;
        }
        //Underground only: below the terrain surface of the column, so any cave or tunnel works, the surface does not
        if(level.canSeeSky(event.getEntity().blockPosition())){
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
            return;
        }
        //Do not crowd a single cave: no more than a few of them around the spawn position
        if(countNearbyUrchinkin(level,event.getEntity()) >= MAX_URCHINKIN_NEARBY){
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }

    private static int countNearbyUrchinkin(ServerLevel level, Mob mob){
        return level.getEntitiesOfClass(Mob.class,mob.getBoundingBox().inflate(CROWD_RADIUS),entity -> entity.getType() == CDPSpawns.urchinkin()).size();
    }

    private static boolean isUnknownDimension(ServerLevel level){
        return level.dimension().equals(CDPDimension.UNKNOWN.getLevel());
    }
}
