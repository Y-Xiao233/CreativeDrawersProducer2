package net.yxiao233.cdp2.api.registry;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers neoforge:add_spawns biome modifiers, so a mob can be added to the natural spawn list of the given biomes.
 * The spawn category is taken from the entity type itself.
 */
public class CDPSpawnBiomeModifiersRegister {
    private static final ArrayList<CDPSpawnBiomeModifiersRegister> MODIFIERS = new ArrayList<>();
    private final ResourceKey<BiomeModifier> biomeModifier;
    private final List<ResourceKey<Biome>> biomes;
    private final EntityType<?> entityType;
    private final int weight;
    private final int minCount;
    private final int maxCount;

    private CDPSpawnBiomeModifiersRegister(ResourceKey<BiomeModifier> biomeModifier, List<ResourceKey<Biome>> biomes, EntityType<?> entityType, int weight, int minCount, int maxCount){
        this.biomeModifier = biomeModifier;
        this.biomes = List.copyOf(biomes);
        this.entityType = entityType;
        this.weight = weight;
        this.minCount = minCount;
        this.maxCount = maxCount;
        MODIFIERS.add(this);
    }

    public static CDPSpawnBiomeModifiersRegister registrySimple(String name, List<ResourceKey<Biome>> biomes, EntityType<?> entityType, int weight, int minCount, int maxCount){
        return new CDPSpawnBiomeModifiersRegister(registryKey(name),biomes,entityType,weight,minCount,maxCount);
    }

    public static void registry(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);

        MODIFIERS.forEach(holder -> {
            HolderSet<Biome> biomeSet = HolderSet.direct(holder.biomes.stream().map(biomeGetter::getOrThrow).toList());
            MobSpawnSettings.SpawnerData spawnerData = new MobSpawnSettings.SpawnerData(holder.entityType,holder.weight,holder.minCount,holder.maxCount);
            context.register(holder.biomeModifier,new BiomeModifiers.AddSpawnsBiomeModifier(biomeSet,List.of(spawnerData)));
        });
    }

    private static ResourceKey<BiomeModifier> registryKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CreativeDrawersProducer2.makeId(name));
    }

    public ResourceKey<BiomeModifier> getBiomeModifier() {
        return biomeModifier;
    }
}
