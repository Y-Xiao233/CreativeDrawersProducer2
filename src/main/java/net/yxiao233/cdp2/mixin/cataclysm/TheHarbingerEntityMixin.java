package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Harbinger_Entity;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * The harbinger is activated by using a vanilla nether star on it. This replaces that hardcoded item with the one
 * configured through kubejs.
 */
@Mixin(The_Harbinger_Entity.class)
public abstract class TheHarbingerEntityMixin {
    @WrapOperation(
            method = "mobInteract",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/item/Items;NETHER_STAR:Lnet/minecraft/world/item/Item;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private Item cdp2$activationItem(Operation<Item> original){
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.THE_HARBINGER);
        return configured == null ? original.call() : configured;
    }
}
