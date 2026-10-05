package net.yxiao233.cdp2.common.registry;

import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.yxiao233.cdp2.api.registry.CDPSpawnBiomeModifiersRegister;

/**
 * Natural mob spawns of the unknown dimension. The mobs are added to the biome spawn lists by
 * {@link CDPSpawnBiomeModifiersRegister}, the extra spawn rules used for them live in
 * {@link net.yxiao233.cdp2.common.event.SpawnEvent}.
 */
public class CDPSpawns {
    /** Weight and pack size are the same as a vanilla hostile mob */
    public static final CDPSpawnBiomeModifiersRegister URCHINKIN = CDPSpawnBiomeModifiersRegister.registrySimple(
            "urchinkin_spawn",CDPBiomes.UNKNOWN_BIOMES,urchinkin(),100,1,4);

    public static EntityType<?> urchinkin(){
        return ModEntities.URCHINKIN.get();
    }

    public static void init(){}
}
