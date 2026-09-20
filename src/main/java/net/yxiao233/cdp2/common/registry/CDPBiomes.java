package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.worldgen.CDPRiverFeatures;
import net.yxiao233.cdp2.worldgen.CDPTreeFeatures;

public class CDPBiomes {
    public static final ResourceKey<Biome> QUORVETH = ResourceKey.create(Registries.BIOME, CreativeDrawersProducer2.makeId("quorveth"));
    public static final ResourceKey<Biome> MYRKHAL = ResourceKey.create(Registries.BIOME, CreativeDrawersProducer2.makeId("myrkhal"));

    public static void boostrap(BootstrapContext<Biome> context){
        context.register(QUORVETH,createBiome(context,0x78A7FF,0x77AB2F,0x77AB2F,0xC0D8FF));
        context.register(MYRKHAL,createBiome(context,0x2E2E5A,0x5F8A5F,0x4F7A4F,0x2A2A44));
    }

    private static Biome createBiome(BootstrapContext<Biome> context, int skyColor, int grassColor, int foliageColor, int fogColor) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        biomeBuilder.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, context.lookup(Registries.PLACED_FEATURE).getOrThrow(CDPRiverFeatures.RIVER_FLUID_PLACED));
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, context.lookup(Registries.PLACED_FEATURE).getOrThrow(CDPTreeFeatures.TREES_PLACED));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .mobSpawnSettings(spawnBuilder.build())
                .downfall(0.0f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(skyColor)
                        .grassColorOverride(grassColor)
                        .foliageColorOverride(foliageColor)
                        .fogColor(fogColor)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic((Music) null)
                        .build()
                ).build();
    }
}
