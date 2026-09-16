package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPBiomes;
import net.yxiao233.cdp2.common.registry.CDPDimension;
import net.yxiao233.cdp2.worldgen.CDPBiomeModifiers;
import net.yxiao233.cdp2.worldgen.CDPConfigureFeatures;
import net.yxiao233.cdp2.worldgen.CDPNoiseSettings;
import net.yxiao233.cdp2.worldgen.CDPPlacedFeatures;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CDPWorldGenProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, CDPDimension::bootstrapType)
            .add(Registries.LEVEL_STEM, CDPDimension::bootstrapStem)
            .add(Registries.CONFIGURED_FEATURE, CDPConfigureFeatures::boostrap)
            .add(Registries.PLACED_FEATURE, CDPPlacedFeatures::bootstrap)
            .add(Registries.NOISE_SETTINGS, CDPNoiseSettings::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, CDPBiomeModifiers::bootstrap)
            .add(Registries.BIOME, CDPBiomes::boostrap);
    public CDPWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CreativeDrawersProducer2.MODID));
    }
}
