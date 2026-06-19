package net.yxiao233.cdp2.mixin.mekanism;

import mekanism.api.Upgrade;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(TileComponentUpgrade.class)
public interface TileComponentUpgradeAccessor {
    @Accessor("upgrades")
    Map<Upgrade, Integer> getUpgradeMap();
    @Accessor("tile")
    TileEntityMekanism getTileEntity();
}
