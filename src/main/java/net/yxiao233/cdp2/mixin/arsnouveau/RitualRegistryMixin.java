package net.yxiao233.cdp2.mixin.arsnouveau;

import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitual;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(RitualRegistry.class)
public class RitualRegistryMixin {

    @Shadow private static ConcurrentHashMap<ResourceLocation, AbstractRitual> ritualMap;

    /**
     * @author Y_Xiao233
     * @reason mixin
     */
    @Overwrite
    public static @Nullable AbstractRitual getRitual(ResourceLocation id) {
        if (!ritualMap.containsKey(id))
            return null;
        try {
            if(ritualMap.get(id) instanceof StructureRitual structureRitual){
                return ritualMap.get(id).getClass().getDeclaredConstructor(String.class, String.class).newInstance(structureRitual.name, structureRitual.nbtPath);
            }
            return ritualMap.get(id).getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
