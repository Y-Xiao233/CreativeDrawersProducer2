package net.yxiao233.cdp2.mixin.mekanism;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.Upgrade;
import mekanism.common.inventory.slot.UpgradeInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.util.UpgradeUtils;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TileComponentUpgrade.class)
public abstract class TileComponentUpgradeMixin {
    @Shadow public abstract int getUpgrades(Upgrade upgrade);
    @Shadow @Final private Map<Upgrade, Integer> upgrades;

    @Shadow @Final private TileEntityMekanism tile;

    /**
     *
     * @reason  让玩家无法在机器升级页面手动安装升级
     */
    @Inject(at = @At("HEAD"), method = "tickServer", cancellable = true)
    private void cdp2$cancelInstall(CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * @author Y_Xiao233
     * @reason  让玩家无法在机器升级页面手动卸载升级
     */
    @Overwrite
    public void removeUpgrade(Upgrade upgrade, boolean removeAll){
        int installed = this.getUpgrades(upgrade);
        if (installed > 0) {
            if(this.upgrades.containsKey(upgrade)){
                this.upgrades.remove(upgrade);
                this.tile.recalculateUpgrades(upgrade);
            }
        }
    }
}
