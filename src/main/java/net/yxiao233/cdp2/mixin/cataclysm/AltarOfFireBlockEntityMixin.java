package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.blockentities.AltarOfFire_Block_Entity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.item.Item;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The altar of fire activates Ignis when its slot holds {@code cataclysm:burning_ashes}. This replaces that hardcoded
 * item with the one configured through kubejs, so the altar accepts anything the pack author wants.
 */
@Mixin(AltarOfFire_Block_Entity.class)
public abstract class AltarOfFireBlockEntityMixin {
    @ModifyExpressionValue(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/registries/DeferredItem;get()Ljava/lang/Object;"
            )
    )
    private Object cdp2$activationItem(Object original){
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.IGNIS);
        return configured == null ? original : configured;
    }
}
