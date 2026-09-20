package net.yxiao233.cdp2.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.List;

public class CDPNoiseSettings {
    private static final int MIN_Y = -64;
    private static final int HEIGHT = 256;
    private static final int SEA_LEVEL = 40;
    private static final int RIVER_BED_LEVEL = 34;
    private static final int PEAK_LIMIT = 120;
    private static final double BASE_HEIGHT = 50.0;
    private static final double BASE_HEIGHT_VARIATION = 5.0;
    private static final double LAND_DETAIL_HEIGHT = 4.0;
    private static final double LAND_DETAIL_SCALE = 0.2;
    private static final double LAND_LIMIT = 58.0;
    private static final double PEAK_START = 0.35;
    private static final double PEAK_HEIGHT = 56.0;
    private static final double MOUNTAIN_DETAIL_HEIGHT = 8.0;
    private static final double JAGGED_HEIGHT = 4.0;
    private static final double JAGGED_SCALE = 1500.0;
    private static final double RIVER_WIDTH = 0.10;
    private static final double DENSITY_SCALE = 0.1;
    private static final double CAVE_THRESHOLD = 0.6;
    private static final double CAVE_DISABLE = 8.0;

    public static final ResourceKey<NoiseGeneratorSettings> UNKNOWN = ResourceKey.create(Registries.NOISE_SETTINGS, CreativeDrawersProducer2.makeId("unknown"));

    private static final SurfaceRules.RuleSource SURFACE_RULE = SurfaceRules.sequence(
            SurfaceRules.ifTrue(
                    SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.bottom()),
                    SurfaceRules.state(Blocks.BEDROCK.defaultBlockState())
            ),
            SurfaceRules.ifTrue(
                    SurfaceRules.abovePreliminarySurface(),
                    SurfaceRules.ifTrue(
                            SurfaceRules.ON_FLOOR,
                            SurfaceRules.state(blockState("malum:blighted_earth"))
                    )
            )
    );

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context){
        context.register(UNKNOWN, new NoiseGeneratorSettings(
                NoiseSettings.create(MIN_Y,HEIGHT,1,2),
                blockState("malum:twisted_rock"),
                Blocks.AIR.defaultBlockState(),
                createRouter(context.lookup(Registries.NOISE)),
                SURFACE_RULE,
                List.of(),
                SEA_LEVEL,
                true,
                false,
                false,
                false
        ));
    }

    private static NoiseRouter createRouter(HolderGetter<NormalNoise.NoiseParameters> noises){
        DensityFunction zero = DensityFunctions.zero();
        DensityFunction continents = DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(Noises.CONTINENTALNESS),0.25,0.0));
        DensityFunction erosion = DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(Noises.EROSION),0.25,0.0));
        DensityFunction ridges = DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(Noises.RIDGE),0.25,0.0));
        DensityFunction hills = DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(Noises.SURFACE),LAND_DETAIL_SCALE,0.0));
        DensityFunction jagged = DensityFunctions.noise(noises.getOrThrow(Noises.JAGGED),JAGGED_SCALE,0.0);
        DensityFunction ridgesAbs = DensityFunctions.max(ridges,DensityFunctions.mul(ridges,DensityFunctions.constant(-1.0)));

        DensityFunction base = DensityFunctions.add(
                DensityFunctions.constant(BASE_HEIGHT),
                DensityFunctions.mul(continents,DensityFunctions.constant(BASE_HEIGHT_VARIATION))
        );

        DensityFunction land = DensityFunctions.min(
                DensityFunctions.add(base,DensityFunctions.mul(hills,DensityFunctions.constant(LAND_DETAIL_HEIGHT))),
                DensityFunctions.constant(LAND_LIMIT)
        );

        DensityFunction mountainMask = DensityFunctions.mul(
                DensityFunctions.add(DensityFunctions.constant(1.0),DensityFunctions.mul(erosion,DensityFunctions.constant(-1.0))),
                DensityFunctions.constant(0.5)
        );

        DensityFunction peakMask = clamp(
                DensityFunctions.mul(
                        DensityFunctions.add(ridgesAbs,DensityFunctions.constant(-PEAK_START)),
                        DensityFunctions.constant(1.0 / (1.0 - PEAK_START))
                ),
                0.0,1.0
        );

        DensityFunction mountainDetail = DensityFunctions.add(
                DensityFunctions.constant(PEAK_HEIGHT),
                DensityFunctions.add(
                        DensityFunctions.mul(hills,DensityFunctions.constant(MOUNTAIN_DETAIL_HEIGHT)),
                        DensityFunctions.mul(jagged,DensityFunctions.constant(JAGGED_HEIGHT))
                )
        );

        DensityFunction mountains = DensityFunctions.mul(
                peakMask,
                DensityFunctions.mul(mountainMask,mountainDetail)
        );

        DensityFunction relief = DensityFunctions.add(land,mountains);

        DensityFunction riverMask = clamp(
                DensityFunctions.mul(ridgesAbs,DensityFunctions.constant(1.0 / RIVER_WIDTH)),
                0.0,1.0
        );

        DensityFunction height = DensityFunctions.min(
                DensityFunctions.lerp(riverMask,RIVER_BED_LEVEL,relief),
                DensityFunctions.constant(PEAK_LIMIT)
        );

        DensityFunction surfaceDensity = DensityFunctions.mul(
                DensityFunctions.add(
                        height,
                        DensityFunctions.yClampedGradient(MIN_Y,MIN_Y + HEIGHT,-MIN_Y,-(MIN_Y + HEIGHT))
                ),
                DensityFunctions.constant(DENSITY_SCALE)
        );

        DensityFunction caveNoise = DensityFunctions.noise(noises.getOrThrow(Noises.CAVE_CHEESE),1.0,1.0);
        DensityFunction caveMask = DensityFunctions.yClampedGradient(16,48,1.0,0.0);
        DensityFunction caveDensity = DensityFunctions.add(
                DensityFunctions.mul(
                        DensityFunctions.add(DensityFunctions.constant(CAVE_THRESHOLD),DensityFunctions.mul(caveNoise,DensityFunctions.constant(-1.0))),
                        caveMask
                ),
                DensityFunctions.mul(
                        DensityFunctions.add(DensityFunctions.constant(1.0),DensityFunctions.mul(caveMask,DensityFunctions.constant(-1.0))),
                        DensityFunctions.constant(CAVE_DISABLE)
                )
        );

        DensityFunction bottomSolid = DensityFunctions.yClampedGradient(MIN_Y,MIN_Y + 8,4.0,-4.0);
        DensityFunction density = DensityFunctions.max(DensityFunctions.min(surfaceDensity,caveDensity),bottomSolid);

        return new NoiseRouter(
                zero,
                zero,
                zero,
                zero,
                zero,
                zero,
                continents,
                erosion,
                zero,
                ridges,
                density,
                DensityFunctions.interpolated(density),
                zero,
                zero,
                zero
        );
    }

    private static DensityFunction clamp(DensityFunction function, double min, double max){
        return DensityFunctions.min(
                DensityFunctions.max(function,DensityFunctions.constant(min)),
                DensityFunctions.constant(max)
        );
    }

    private static BlockState blockState(String id){
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
        if(block == Blocks.AIR){
            throw new IllegalStateException("Missing block: " + id);
        }
        return block.defaultBlockState();
    }
}
