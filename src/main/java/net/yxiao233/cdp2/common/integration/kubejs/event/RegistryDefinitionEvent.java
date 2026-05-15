package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import io.github.lounode.ae2cs.common.item.CrystalSeedItem;
import net.minecraft.world.item.Item;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitualDefinition;
import net.yxiao233.cdp2.common.integration.kubejs.util.KubeUtils;
import net.yxiao233.cdp2.mixin.ae2cs.AECSAccessor;

@SuppressWarnings("unused")
public class RegistryDefinitionEvent implements KubeEvent {
    public static void createStructureRitual(String registryName, String nbtPath){
        StructureRitualDefinition.create(registryName,nbtPath);
    }

    public static void createCrystalSeedItem(String name, Object growTo){
        AECSAccessor.callRegistryCrystalSeedItem(name, () -> new CrystalSeedItem(new Item.Properties(), KubeUtils.getItemFromObject(growTo)));
    }
}
