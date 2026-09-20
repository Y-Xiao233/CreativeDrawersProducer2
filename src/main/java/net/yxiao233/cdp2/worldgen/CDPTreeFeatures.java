package net.yxiao233.cdp2.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.SurfaceWaterDepthFilter;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.List;

public class CDPTreeFeatures {
    private static final IntProvider TREES_PER_CHUNK = new WeightedListInt(
            SimpleWeightedRandomList.<IntProvider>builder()
                    .add(ConstantInt.of(0), 8)
                    .add(ConstantInt.of(1), 1)
                    .add(ConstantInt.of(2), 1)
                    .build()
    );

    public static final ResourceKey<ConfiguredFeature<?,?>> TREES = configuredKey("trees");

    public static final ResourceKey<PlacedFeature> SOULWOOD_TREE = placedKey("soulwood_tree");
    public static final ResourceKey<PlacedFeature> AZURE_RUNEWOOD_TREE = placedKey("azure_runewood_tree");
    public static final ResourceKey<PlacedFeature> RUNEWOOD_TREE = placedKey("runewood_tree");
    public static final ResourceKey<PlacedFeature> TREES_PLACED = placedKey("trees");

    private static final ResourceKey<ConfiguredFeature<?,?>> MALUM_SOULWOOD = malumConfiguredKey("soulwood_tree");
    private static final ResourceKey<ConfiguredFeature<?,?>> MALUM_AZURE_RUNEWOOD = malumConfiguredKey("azure_runewood_tree");
    private static final ResourceKey<ConfiguredFeature<?,?>> MALUM_RUNEWOOD = malumConfiguredKey("runewood_tree");

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?,?>> context){
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        context.register(TREES, new ConfiguredFeature<>(Feature.RANDOM_SELECTOR, new RandomFeatureConfiguration(
                List.of(
                        new WeightedPlacedFeature(placed.getOrThrow(SOULWOOD_TREE),0.35f),
                        new WeightedPlacedFeature(placed.getOrThrow(AZURE_RUNEWOOD_TREE),0.5f),
                        new WeightedPlacedFeature(placed.getOrThrow(RUNEWOOD_TREE),1.0f)
                ),
                placed.getOrThrow(SOULWOOD_TREE)
        )));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?,?>> configured = context.lookup(Registries.CONFIGURED_FEATURE);
        context.register(SOULWOOD_TREE, new PlacedFeature(configured.getOrThrow(MALUM_SOULWOOD), List.of()));
        context.register(AZURE_RUNEWOOD_TREE, new PlacedFeature(configured.getOrThrow(MALUM_AZURE_RUNEWOOD), List.of()));
        context.register(RUNEWOOD_TREE, new PlacedFeature(configured.getOrThrow(MALUM_RUNEWOOD), List.of()));
        context.register(TREES_PLACED, new PlacedFeature(configured.getOrThrow(TREES), List.of(
                CountPlacement.of(TREES_PER_CHUNK),
                InSquarePlacement.spread(),
                SurfaceWaterDepthFilter.forMaxDepth(0),
                HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                BiomeFilter.biome()
        )));
    }

    private static ResourceKey<ConfiguredFeature<?,?>> configuredKey(String path){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, CreativeDrawersProducer2.makeId(path));
    }

    private static ResourceKey<PlacedFeature> placedKey(String path){
        return ResourceKey.create(Registries.PLACED_FEATURE, CreativeDrawersProducer2.makeId(path));
    }

    private static ResourceKey<ConfiguredFeature<?,?>> malumConfiguredKey(String path){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("malum", path));
    }
}
