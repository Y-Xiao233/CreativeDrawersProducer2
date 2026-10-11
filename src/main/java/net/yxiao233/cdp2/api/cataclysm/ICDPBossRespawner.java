package net.yxiao233.cdp2.api.cataclysm;

import org.jetbrains.annotations.Nullable;

/**
 * Implemented (through a mixin) by Cataclysm's boss respawner block entity, it tells which configuration key of
 * {@link net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems} belongs to the boss it spawns.
 */
public interface ICDPBossRespawner {
    @Nullable
    String cdp2$activationKey();
}
