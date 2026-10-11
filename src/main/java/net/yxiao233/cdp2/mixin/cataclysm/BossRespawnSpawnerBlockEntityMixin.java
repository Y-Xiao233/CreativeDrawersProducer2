package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.blockentities.Boss_Respawn_Spawner_Block_Entity;
import net.minecraft.world.entity.EntityType;
import net.yxiao233.cdp2.api.cataclysm.ICDPBossRespawner;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

/**
 * Tells which kubejs configuration key belongs to the boss that respawner spawns, so its activation item can be
 * replaced per boss.
 */
@Mixin(Boss_Respawn_Spawner_Block_Entity.class)
public abstract class BossRespawnSpawnerBlockEntityMixin implements ICDPBossRespawner {
    @Shadow
    private EntityType<?> spawnType;

    @Override
    @Unique
    public String cdp2$activationKey(){
        return CDPCataclysmItems.keyOf(this.spawnType);
    }
}
