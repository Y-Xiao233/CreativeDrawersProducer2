package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.blocks.Boss_Respawn_Spawner_Block;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.yxiao233.cdp2.api.cataclysm.ICDPBossRespawner;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * A boss respawner (the cage that brings a boss back) is activated with the item stored inside it. This additionally
 * accepts the item configured through kubejs for the boss that respawner belongs to.
 */
@Mixin(Boss_Respawn_Spawner_Block.class)
public abstract class BossRespawnSpawnerBlockMixin {
    @ModifyExpressionValue(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
    )
    private boolean cdp2$configuredItem(boolean original, ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult){
        if(original){
            return true;
        }
        if(!(level.getBlockEntity(pos) instanceof ICDPBossRespawner respawner)){
            return false;
        }
        Item configured = CDPCataclysmItems.get(respawner.cdp2$activationKey());
        return configured != null && stack.is(configured);
    }
}
