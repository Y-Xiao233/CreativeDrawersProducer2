package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.blocks.Door_of_Seal_Block;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.Item;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The door of seal opens with {@code cataclysm:strange_key}. This replaces that hardcoded item with the one configured
 * through kubejs.
 */
@Mixin(Door_of_Seal_Block.class)
public abstract class DoorOfSealBlockMixin {
    @ModifyExpressionValue(
            method = "useItemOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/registries/DeferredItem;get()Ljava/lang/Object;"
            )
    )
    private Object cdp2$openingItem(Object original){
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.DOOR_OF_SEAL);
        return configured == null ? original : configured;
    }
}
