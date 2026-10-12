package net.yxiao233.cdp2.common.integration.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Harbinger_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.Ancient_Remnant.Ancient_Remnant_Entity;
import com.github.L_Ender.cataclysm.entity.InternalAnimationMonster.IABossMonsters.NewNetherite_Monstrosity.Netherite_Monstrosity_Entity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Knows which Cataclysm bosses are still asleep in their structure and therefore must not be damageable nor be moved
 * around (the Ars Nouveau containment ritual for example).
 *
 * <p>A boss counts as asleep until the item configured through kubejs was used on it: the netherite monstrosity wakes
 * up through {@code setIsAwaken}, the harbinger through {@code setIsAct} and the ancient remnant through
 * {@code setNecklace}.</p>
 */
public class CDPCataclysmBosses {
    /** {@return true when the entity is a Cataclysm boss that has not been activated yet} */
    public static boolean isAsleep(LivingEntity entity){
        if(entity instanceof Netherite_Monstrosity_Entity monstrosity){
            return !monstrosity.getIsAwaken();
        }
        if(entity instanceof The_Harbinger_Entity harbinger){
            return !harbinger.getIsAct();
        }
        if(entity instanceof Ancient_Remnant_Entity remnant){
            return !remnant.getNecklace();
        }
        return false;
    }

    /** {@return true when that entity is a Cataclysm boss that has not been activated yet} */
    public static boolean isAsleep(Entity entity){
        return entity instanceof LivingEntity living && isAsleep(living);
    }
}
