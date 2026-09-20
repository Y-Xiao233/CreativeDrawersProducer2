package net.yxiao233.cdp2.api.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class CDPDimensionRegister {
    private static final DimensionType DEFAULT_TYPE = new DimensionType(
            OptionalLong.of(6000),
            true,false,false,false,1.0,true,false,
            -64,16,16,
            BlockTags.INFINIBURN_OVERWORLD,
            BuiltinDimensionTypes.OVERWORLD_EFFECTS,
            1.0f,
            new DimensionType.MonsterSettings(false,false, ConstantInt.of(0),0)
    );
    private static final DimensionType DEFAULT_NOISE_TYPE = new DimensionType(
            OptionalLong.of(18000),
            true,false,false,false,1.0,true,false,
            -64,256,256,
            BlockTags.INFINIBURN_OVERWORLD,
            BuiltinDimensionTypes.OVERWORLD_EFFECTS,
            0.0f,
            new DimensionType.MonsterSettings(false,false, ConstantInt.of(0),0)
    );
    private static final ArrayList<CDPDimensionRegister> DIMENSIONS = new ArrayList<>();
    private final ResourceKey<Level> level;
    private final ResourceKey<LevelStem> levelStem;
    private final ResourceKey<DimensionType> dimensionType;
    private final DimensionType type;
    private final ResourceKey<Biome> biome;
    private final List<Pair<Climate.ParameterPoint,ResourceKey<Biome>>> biomeParameters;
    private final List<FlatLayerInfo> info;
    private final ResourceKey<NoiseGeneratorSettings> noiseSettings;
    private CDPDimensionRegister(ResourceKey<Level> level, ResourceKey<LevelStem> levelStem, ResourceKey<DimensionType> dimensionType, DimensionType type, ResourceKey<Biome> biome, List<Pair<Climate.ParameterPoint,ResourceKey<Biome>>> biomeParameters, List<FlatLayerInfo> info, ResourceKey<NoiseGeneratorSettings> noiseSettings){
        this.level = level;
        this.levelStem = levelStem;
        this.dimensionType = dimensionType;
        this.type = type;
        this.biome = biome;
        this.biomeParameters = biomeParameters == null ? null : List.copyOf(biomeParameters);
        this.info = List.copyOf(info);
        this.noiseSettings = noiseSettings;
        DIMENSIONS.add(this);
    }
    public static CDPDimensionRegister registryFlat(String name, DimensionType type, ResourceKey<Biome> biome, List<FlatLayerInfo> info){
        return new CDPDimensionRegister(registryLevel(name),registryLevelStem(name),registryType(name),type,biome,null,info,null);
    }

    public static CDPDimensionRegister registryDefault(String name, ResourceKey<Biome> biome, List<FlatLayerInfo> info){
        return registryFlat(name,DEFAULT_TYPE,biome,info);
    }

    public static CDPDimensionRegister registryNoise(String name, DimensionType type, ResourceKey<Biome> biome, ResourceKey<NoiseGeneratorSettings> noiseSettings){
        return new CDPDimensionRegister(registryLevel(name),registryLevelStem(name),registryType(name),type,biome,null,List.of(),noiseSettings);
    }

    public static CDPDimensionRegister registryNoise(String name, ResourceKey<Biome> biome, ResourceKey<NoiseGeneratorSettings> noiseSettings){
        return registryNoise(name,DEFAULT_NOISE_TYPE,biome,noiseSettings);
    }

    public static CDPDimensionRegister registryNoise(String name, DimensionType type, List<Pair<Climate.ParameterPoint,ResourceKey<Biome>>> biomeParameters, ResourceKey<NoiseGeneratorSettings> noiseSettings){
        return new CDPDimensionRegister(registryLevel(name),registryLevelStem(name),registryType(name),type,null,biomeParameters,List.of(),noiseSettings);
    }

    public static CDPDimensionRegister registryNoise(String name, List<Pair<Climate.ParameterPoint,ResourceKey<Biome>>> biomeParameters, ResourceKey<NoiseGeneratorSettings> noiseSettings){
        return registryNoise(name,DEFAULT_NOISE_TYPE,biomeParameters,noiseSettings);
    }

    public static List<Pair<Climate.ParameterPoint,ResourceKey<Biome>>> singleBiome(ResourceKey<Biome> biome){
        return List.of(Pair.of(allClimate(),biome));
    }

    public static Climate.ParameterPoint allClimate(){
        return allClimate(0.0f);
    }

    public static Climate.ParameterPoint allClimate(float offset){
        return Climate.parameters(span(-1.0f,1.0f),span(-1.0f,1.0f),span(-1.0f,1.0f),span(-1.0f,1.0f),span(-1.0f,1.0f),span(-1.0f,1.0f),offset);
    }

    private static Climate.Parameter span(float min, float max){
        return Climate.Parameter.span(min,max);
    }

    private static ResourceKey<Level> registryLevel(String name){
        return ResourceKey.create(Registries.DIMENSION, CreativeDrawersProducer2.makeId(name));
    }

    private static ResourceKey<LevelStem> registryLevelStem(String name){
        return ResourceKey.create(Registries.LEVEL_STEM, CreativeDrawersProducer2.makeId(name));
    }

    private static ResourceKey<DimensionType> registryType(String name){
        return ResourceKey.create(Registries.DIMENSION_TYPE, CreativeDrawersProducer2.makeId(name + "_type"));
    }

    public static void registryType(BootstrapContext<DimensionType> context){
        DIMENSIONS.forEach(holder -> context.register(holder.dimensionType,holder.type));
    }

    public static void registryLevelStem(BootstrapContext<LevelStem> context){
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimensionType = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        DIMENSIONS.forEach(holder ->{
            ChunkGenerator generator;
            if(holder.noiseSettings != null){
                generator = new NoiseBasedChunkGenerator(createBiomeSource(biomes,holder),noiseSettings.getOrThrow(holder.noiseSettings));
            }else{
                FlatLevelGeneratorSettings settings = new FlatLevelGeneratorSettings(
                        Optional.of(HolderSet.empty()),
                        biomes.getOrThrow(holder.biome),
                        List.of()
                );

                settings.setDecoration();
                settings.getLayersInfo().addAll(holder.info);
                settings.updateLayers();

                generator = new FlatLevelSource(settings);
            }

            LevelStem stem = new LevelStem(dimensionType.getOrThrow(holder.dimensionType),generator);

            context.register(holder.levelStem,stem);
        });
    }

    private static BiomeSource createBiomeSource(HolderGetter<Biome> biomes, CDPDimensionRegister holder){
        if(holder.biomeParameters == null){
            return new FixedBiomeSource(biomes.getOrThrow(holder.biome));
        }
        List<Pair<Climate.ParameterPoint,Holder<Biome>>> parameters = new ArrayList<>(holder.biomeParameters.size());
        holder.biomeParameters.forEach(entry -> parameters.add(Pair.of(entry.getFirst(),biomes.getOrThrow(entry.getSecond()))));
        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(parameters));
    }

    public ResourceKey<DimensionType> getDimensionType() {
        return dimensionType;
    }

    public ResourceKey<Level> getLevel() {
        return level;
    }

    public ResourceKey<LevelStem> getLevelStem() {
        return levelStem;
    }
}
