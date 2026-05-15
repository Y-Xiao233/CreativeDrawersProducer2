package net.yxiao233.cdp2.common.integration.arsnouveau;

import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;

import java.util.HashMap;
import java.util.Map;

public class StructureRitualDefinition{
    private static final Map<String, String> definitions = new HashMap<>();
    public static void create(String registryName, String nbtPath){
        definitions.put(registryName,nbtPath);
    }

    public static void registryAll(){
        definitions.forEach((name, path) -> {
            APIRegistry.registerRitual(new StructureRitual(name,path));
        });
    }
}
