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

/**
 * The netherite monstrosity only wakes up on its own once a player comes close to it ({@code NMDoNothingGoal}), it
 * has no interaction of its own. This gives it the same activation as the harbinger: using the configured item on it
 * wakes it up ({@code setIsAwaken(true)}, exactly what that goal does) and consumes the item.
 *
 * <p>While it is still asleep it cannot be attacked either (see {@code minecraft.LivingEntityMixin}), so the only way
 * to get it going is the configured item.</p>
 *
 * <p>The item defaults to a vanilla nether star and can be replaced through kubejs like the other bosses.</p>
 */
@Mixin(Netherite_Monstrosity_Entity.class)
public abstract class NetheriteMonstrosityEntityMixin {
    @Shadow
    public abstract boolean getIsAwaken();

    @Shadow
    public abstract void setIsAwaken(boolean awaken);

    public InteractionResult mobInteract(Player player, InteractionHand hand){
        ItemStack stack = player.getItemInHand(hand);
        Item configured = CDPCataclysmItems.get(CDPCataclysmItems.NETHERITE_MONSTROSITY,Items.NETHER_STAR);
        if(!this.getIsAwaken() && !stack.isEmpty() && stack.getItem() == configured){
            if(!player.isCreative()){
                stack.shrink(1);
            }
            this.setIsAwaken(true);
            //The same state the vanilla awaken goal switches to (setAttackState lives in the super class)
            ((Internal_Animation_Monster)(Object)this).setAttackState(2);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
