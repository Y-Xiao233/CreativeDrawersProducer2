package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.Internal_Animation_Monster;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The netherite monstrosity wakes up on its own as soon as a player gets close to it ({@code NMDoNothingGoal}), it has
 * no interaction of its own. This replaces that with the same activation the harbinger has: using the configured item
 * on it wakes it up ({@code setIsAwaken(true)}, exactly what that goal does) and consumes the item.
 *
 * <p>Every automatic awakening is blocked, so the only way to get it going is that item. While asleep it cannot be
 * attacked either (see {@code minecraft.LivingEntityMixin}) and it stays completely idle
 * ({@code NetheriteMonstrosityDoNothingGoalMixin}).</p>
 *
 * <p>The item defaults to a vanilla nether star and can be replaced through kubejs like the other bosses.</p>
 */
@Mixin(Netherite_Monstrosity_Entity.class)
public abstract class NetheriteMonstrosityEntityMixin {
    /** Only true while the configured item is used, it lets that one call through the guard below */
    @Unique
    private boolean cdp2$awakeningByItem;

    @Shadow
    public abstract boolean getIsAwaken();

    @Shadow
    public abstract void setIsAwaken(boolean awaken);

    /** Getting close, being attacked or any other automatic path must not awaken the monstrosity */
    @Inject(
            method = "setIsAwaken",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cdp2$blockAutomaticAwaken(boolean awaken, CallbackInfo ci){
        if(awaken && !cdp2$awakeningByItem){
            ci.cancel();
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.NETHERITE_MONSTROSITY,Items.NETHER_STAR);
        if(!this.getIsAwaken() && !stack.isEmpty() && stack.getItem() == configured){
            if(!player.isCreative()){
                stack.shrink(1);
            }
            cdp2$awakeningByItem = true;
            try{
                this.setIsAwaken(true);
            }finally{
                cdp2$awakeningByItem = false;
            }
            //The same state the vanilla awaken goal switches to (setAttackState lives in the super class)
            ((Internal_Animation_Monster)(Object)this).setAttackState(2);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
