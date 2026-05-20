package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import io.github.lounode.ae2cs.common.item.CrystalSeedItem;
import net.minecraft.world.item.Item;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitualDefinition;
import net.yxiao233.cdp2.common.integration.kubejs.util.KubeUtils;
import net.yxiao233.cdp2.mixin.ae2cs.AECSAccessor;

@SuppressWarnings("unused")
public class ItemRegistryEvent implements KubeEvent {
    public static void createStructureRitual(String registryName, String nbtPath, int xOffset, int yOffset, int zOffset){
        StructureRitualDefinition.create(registryName,nbtPath,xOffset,yOffset,zOffset);
    }

    public static void createStructureRitual(String registryName, String nbtPath){
        createStructureRitual(registryName,nbtPath,0,0,0);
    }

    public static void createCrystalSeedItem(String name, Object growTo){
        AECSAccessor.callRegistryCrystalSeedItem(name, () -> new CrystalSeedItem(new Item.Properties(), KubeUtils.getItemFromObject(growTo)));
    }
}
