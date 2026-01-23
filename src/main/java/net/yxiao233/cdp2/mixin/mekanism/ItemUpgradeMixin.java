package net.yxiao233.cdp2.mixin.mekanism;

import mekanism.api.Upgrade;
import mekanism.common.item.ItemUpgrade;
import mekanism.common.tile.component.TileComponentUpgrade;
import mekanism.common.tile.interfaces.IUpgradeTile;
import mekanism.common.util.WorldUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemUpgrade.class)
public class ItemUpgradeMixin {
    /**
     *
     * @reason  让玩家无法手持升级shift右键机器安装升级,并提示玩家应该如何升级机器
     */
    @Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
    private void cdp2$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir){
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (player != null && player.isShiftKeyDown() && level.isClientSide()) {
            BlockEntity tile = WorldUtils.getTileEntity(level, context.getClickedPos());
            if (tile instanceof IUpgradeTile) {
                player.sendSystemMessage(Component.translatable("tip.cdp2.mek_upgrade").withStyle(ChatFormatting.RED));
            }
        }
        cir.setReturnValue(InteractionResult.PASS);
    }
}
