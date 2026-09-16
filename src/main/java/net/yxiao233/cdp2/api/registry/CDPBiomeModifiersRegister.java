package net.yxiao233.cdp2.api.registry;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.ArrayList;

public class CDPBiomeModifiersRegister {
    private static final ArrayList<CDPBiomeModifiersRegister> MODIFIERS = new ArrayList<>();
    private final ResourceKey<BiomeModifier> biomeModifier;
    private final ResourceKey<Biome> biomeKey;
    private final TagKey<Biome> biomeTag;
    private final ResourceKey<PlacedFeature> placedFeature;
    private CDPBiomeModifiersRegister(ResourceKey<BiomeModifier> biomeModifier, ResourceKey<Biome> biomeKey, ResourceKey<PlacedFeature> placedFeature){
        this.biomeModifier = biomeModifier;
        this.biomeKey = biomeKey;
        this.placedFeature = placedFeature;
        this.biomeTag = null;
        MODIFIERS.add(this);
    }
    private CDPBiomeModifiersRegister(ResourceKey<BiomeModifier> biomeModifier, TagKey<Biome> biomeTag, ResourceKey<PlacedFeature> placedFeature){
        this.biomeModifier = biomeModifier;
        this.biomeKey = null;
        this.placedFeature = placedFeature;
        this.biomeTag = biomeTag;
        MODIFIERS.add(this);
    }
    public static CDPBiomeModifiersRegister registrySimple(String name, ResourceKey<Biome> biomeKey, ResourceKey<PlacedFeature> placedFeature){
        ResourceKey<BiomeModifier> key = registryKey(name);
        return new CDPBiomeModifiersRegister(key,biomeKey,placedFeature);
    }

    public static CDPBiomeModifiersRegister registrySimple(String name, TagKey<Biome> biomeTag, ResourceKey<PlacedFeature> placedFeature){
        ResourceKey<BiomeModifier> key = registryKey(name);
        return new CDPBiomeModifiersRegister(key,biomeTag,placedFeature);
    }

    public static void registry(BootstrapContext<BiomeModifier> context){
        HolderGetter<PlacedFeature> placedFeature = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biome = context.lookup(Registries.BIOME);

        MODIFIERS.forEach(holder ->{
            HolderSet<Biome> biomeSet = holder.getDefaultBiome(biome);
            if(biomeSet != null){
                context.register(holder.biomeModifier,
                        new BiomeModifiers.AddFeaturesBiomeModifier(
                                biomeSet,
                                HolderSet.direct(placedFeature.getOrThrow(holder.placedFeature)),
                                GenerationStep.Decoration.UNDERGROUND_ORES
                        )
                );
            }
        });
    }

    private HolderSet<Biome> getDefaultBiome(HolderGetter<Biome> getter){
        if(biomeKey == null){
            return getter.getOrThrow(biomeTag);
        }
        if(biomeTag == null){
            return HolderSet.direct(getter.getOrThrow(biomeKey));
        }
        return null;
    }
    private static ResourceKey<BiomeModifier> registryKey(String name){
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CreativeDrawersProducer2.makeId(name));
    }

    public ResourceKey<BiomeModifier> getBiomeModifier() {
        return biomeModifier;
    }
}
