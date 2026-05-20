package net.yxiao233.cdp2.common.integration.arsnouveau;

import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;

public class StructureRitualDefinition{
    private static final Map<Pair<String, String>, Triple<Integer, Integer, Integer>> definitions = new HashMap<>();
    public static void create(String registryName, String nbtPath, int xOffset, int yOffset, int zOffset){
        definitions.put(Pair.of(registryName,nbtPath),Triple.of(xOffset,yOffset,zOffset));
    }

    public static void create(String registryName, String nbtPath){
        create(registryName,nbtPath,0,0,0);
    }

    public static void registryAll(){
        definitions.forEach((n, v) -> {
            APIRegistry.registerRitual(new StructureRitual(n.getKey(),n.getValue(),v.getLeft(),v.getMiddle(),v.getRight()));
        });
    }
}
