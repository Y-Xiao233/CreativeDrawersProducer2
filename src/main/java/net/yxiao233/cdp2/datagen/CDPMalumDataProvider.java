package net.yxiao233.cdp2.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;

public class CDPMalumDataProvider implements DataProvider {
    private final PackOutput.PathProvider structureSets;
    private final PackOutput.PathProvider biomeTags;

    public CDPMalumDataProvider(PackOutput output){
        this.structureSets = output.createPathProvider(PackOutput.Target.DATA_PACK,"worldgen/structure_set");
        this.biomeTags = output.createPathProvider(PackOutput.Target.DATA_PACK,"tags/worldgen/biome");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        JsonObject placement = new JsonObject();
        placement.addProperty("type","minecraft:random_spread");
        placement.addProperty("salt",546451665);
        placement.addProperty("separation",20);
        placement.addProperty("spacing",40);
        placement.addProperty("spread_type","triangular");

        JsonObject entry = new JsonObject();
        entry.addProperty("structure","malum:weeping_well");
        entry.addProperty("weight",1);

        JsonArray structures = new JsonArray();
        structures.add(entry);

        JsonObject structureSet = new JsonObject();
        structureSet.add("placement",placement);
        structureSet.add("structures",structures);

        JsonArray values = new JsonArray();
        values.add("cdp2:quorveth");
        values.add("cdp2:myrkhal");

        JsonObject tag = new JsonObject();
        tag.addProperty("replace",false);
        tag.add("values",values);

        return CompletableFuture.allOf(
                DataProvider.saveStable(cache,structureSet,this.structureSets.json(ResourceLocation.fromNamespaceAndPath("malum","weeping_well"))),
                DataProvider.saveStable(cache,tag,this.biomeTags.json(ResourceLocation.fromNamespaceAndPath("malum","has_weeping_well")))
        );
    }

    @Override
    public String getName(){
        return "CDP Malum Data";
    }
}
