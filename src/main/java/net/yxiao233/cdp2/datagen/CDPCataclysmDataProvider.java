package net.yxiao233.cdp2.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.yxiao233.cdp2.common.registry.CDPBiomes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Appends the unknown dimension's biomes to Cataclysm's structure biome tags, so those structures can generate there.
 * The generated files use {@code "replace": false}, so the biomes Cataclysm already lists (the nether wastes for the
 * burning arena, the desert for the cursed pyramid, ...) are kept as they are.
 *
 * <p>The height of each structure is left to Cataclysm itself: the cursed pyramid and the frosted prison already
 * project their start to {@code WORLD_SURFACE_WG} (surface), the ancient factory starts at Y -30
 * ({@code underground_structures}, so underground) and only the soul black smith needs a fix, see
 * {@code net.yxiao233.cdp2.mixin.cataclysm.CataclysmJigsawStructureMixin}.</p>
 */
public class CDPCataclysmDataProvider implements DataProvider {
    /** Cataclysm structure biome tag -> the unknown dimension biome that should be added to it */
    private static final Map<ResourceLocation,ResourceKey<Biome>> STRUCTURE_BIOMES = new LinkedHashMap<>();
    static {
        STRUCTURE_BIOMES.put(structureBiomeTag("burning_arena_biomes"),CDPBiomes.MYRKHAL);
        STRUCTURE_BIOMES.put(structureBiomeTag("ancient_factory_biomes"),CDPBiomes.QUORVETH);
        STRUCTURE_BIOMES.put(structureBiomeTag("cursed_pyramid_biomes"),CDPBiomes.MYRKHAL);
        STRUCTURE_BIOMES.put(structureBiomeTag("frosted_prison_biomes"),CDPBiomes.MYRKHAL);
        STRUCTURE_BIOMES.put(structureBiomeTag("soul_black_smith_biomes"),CDPBiomes.QUORVETH);
    }

    private final PackOutput.PathProvider biomeTags;

    public CDPCataclysmDataProvider(PackOutput output){
        this.biomeTags = output.createPathProvider(PackOutput.Target.DATA_PACK,"tags/worldgen/biome");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache){
        //TODO temporary: load the mixin target so its transformation runs during datagen, delete after checking
        try{
            Class.forName("com.hollingsworth.arsnouveau.common.ritual.RitualMobCapture",false,CDPCataclysmDataProvider.class.getClassLoader());
        }catch(ClassNotFoundException ignored){
        }

        List<CompletableFuture<?>> futures = new ArrayList<>(STRUCTURE_BIOMES.size());
        STRUCTURE_BIOMES.forEach((tag,biome) -> futures.add(DataProvider.saveStable(cache,biomeTag(biome),this.biomeTags.json(tag))));
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private static JsonObject biomeTag(ResourceKey<Biome> biome){
        JsonArray values = new JsonArray();
        values.add(biome.location().toString());

        JsonObject tag = new JsonObject();
        tag.addProperty("replace",false);
        tag.add("values",values);
        return tag;
    }

    private static ResourceLocation structureBiomeTag(String name){
        return ResourceLocation.fromNamespaceAndPath("cataclysm","has_structure/" + name);
    }

    @Override
    public String getName(){
        return "CDP Cataclysm Data";
    }
}
