package net.yxiao233.cdp2.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPFeature;

import java.util.List;

public class CDPRiverFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> RIVER_FLUID = ResourceKey.create(Registries.CONFIGURED_FEATURE, CreativeDrawersProducer2.makeId("river_fluid"));
    public static final ResourceKey<PlacedFeature> RIVER_FLUID_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, CreativeDrawersProducer2.makeId("river_fluid"));

    public static void bootstrapConfigured(BootstrapContext<ConfiguredFeature<?,?>> context){
        context.register(RIVER_FLUID,new ConfiguredFeature<>(CDPFeature.RIVER_FLUID.get(),NoneFeatureConfiguration.INSTANCE));
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context){
        context.register(RIVER_FLUID_PLACED,new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(RIVER_FLUID),List.of(
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                BiomeFilter.biome()
        )));
    }
}
