package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.world.level.biome.*;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public class CDPBiomes {
    public static final ResourceKey<Biome> UNKNOWN = ResourceKey.create(Registries.BIOME, CreativeDrawersProducer2.makeId("unknown"));

    public static void boostrap(BootstrapContext<Biome> context){
        context.register(UNKNOWN,createBiome(context));
    }

    private static Biome createBiome(BootstrapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .mobSpawnSettings(spawnBuilder.build())
                .downfall(0.0f)
                .temperature(0.8f)
                .generationSettings(biomeBuilder.build())
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .skyColor(0x78A7FF)
                        .grassColorOverride(0x77AB2F)
                        .foliageColorOverride(0x77AB2F)
                        .fogColor(0xC0D8FF)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic((Music) null)
                        .build()
                ).build();
    }
}
