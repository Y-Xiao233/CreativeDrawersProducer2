package net.yxiao233.cdp2.api.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.worldgen.CDPOrePlacement;

import java.util.ArrayList;
import java.util.List;

public class CDPPlacedFeatureRegister {
    private static final ArrayList<CDPPlacedFeatureRegister> FEATURES = new ArrayList<>();
    private final ResourceKey<ConfiguredFeature<?,?>> configuredFeature;
    private final ResourceKey<PlacedFeature> placedFeature;
    private final List<PlacementModifier> modifiers;

    private CDPPlacedFeatureRegister(ResourceKey<ConfiguredFeature<?,?>> configuredFeature, ResourceKey<PlacedFeature> placedFeature, List<PlacementModifier> modifiers){
        this.configuredFeature = configuredFeature;
        this.placedFeature = placedFeature;
        this.modifiers = List.copyOf(modifiers);
        FEATURES.add(this);
    }
    public static CDPPlacedFeatureRegister registrySimple(String name, ResourceKey<ConfiguredFeature<?,?>> feature, List<PlacementModifier> modifiers){
        ResourceKey<PlacedFeature> key = registryKey(name);
        return new CDPPlacedFeatureRegister(feature,key,modifiers);
    }
    public static CDPPlacedFeatureRegister registrySimple(String name, ResourceKey<ConfiguredFeature<?,?>> feature, int count, int min, int max){
        ResourceKey<PlacedFeature> key = registryKey(name);
        return new CDPPlacedFeatureRegister(feature,key, CDPOrePlacement.commonOrePlacement(count, HeightRangePlacement.uniform(VerticalAnchor.absolute(min),VerticalAnchor.absolute(max))));
    }

    private static ResourceKey<PlacedFeature> registryKey(String name){
        return ResourceKey.create(Registries.PLACED_FEATURE, CreativeDrawersProducer2.makeId(name));
    }

    public static void registry(BootstrapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?,?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);
        FEATURES.forEach(holder -> registry(context,holder.placedFeature,configuredFeature.getOrThrow(holder.configuredFeature),holder.modifiers));
    }

    private static void registry(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?,?>> configuredFeature, List<PlacementModifier> modifiers){
        context.register(key,new PlacedFeature(configuredFeature, List.copyOf(modifiers)));
    }

    public ResourceKey<PlacedFeature> getPlacedFeature() {
        return placedFeature;
    }
}
