package net.yxiao233.cdp2.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.yxiao233.cdp2.common.registry.CDPBiomes;

import java.util.Optional;

/**
 * Keeps structures completely out of the unknown dimension's rivers.
 *
 * <p>The rivers are not part of the terrain: {@link CDPRiverFluidFeature} fills every column whose surface is below the
 * sea level (Y 40) with the unknown fluid during the {@code FLUID_SPRINGS} step, which runs after the structures. A
 * structure that touches such a column would be flooded, so the whole footprint of the structure is checked on a grid
 * and the structure is dropped as soon as one column would be filled.</p>
 *
 * <p>Only surface structures are affected, an underground structure like the ancient factory (Y -30) stays where it
 * is.</p>
 */
public final class CDPRiverGuard {
    /** Every column with a surface below this Y is filled with the river fluid by {@link CDPRiverFluidFeature} */
    private static final int RIVER_SURFACE_Y = 40;
    /** Step of the grid used to scan the structure footprint */
    private static final int CHECK_STEP = 8;

    /** {@return true when that column would be filled with river fluid} */
    public static boolean isFlooded(Structure.GenerationContext context, int x, int z){
        return context.chunkGenerator()
                .getBaseHeight(x,z,Heightmap.Types.OCEAN_FLOOR_WG,context.heightAccessor(),context.randomState()) < RIVER_SURFACE_Y;
    }

    /** {@return true when the structure is placed inside one of the unknown dimension's biomes} */
    public static boolean isUnknownDimensionBiome(Structure.GenerationContext context){
        Holder<Biome> biome = biomeAt(context);
        return CDPBiomes.UNKNOWN_BIOMES.stream().anyMatch(biome::is);
    }

    public static Holder<Biome> biomeAt(Structure.GenerationContext context){
        return context.biomeSource()
                .getNoiseBiome(
                        QuartPos.fromBlock(context.chunkPos().getMinBlockX()),
                        QuartPos.fromBlock(0),
                        QuartPos.fromBlock(context.chunkPos().getMinBlockZ()),
                        context.randomState().sampler()
                );
    }

    /**
     * Drops the structure when any column of its footprint (the start chunk plus {@code radius} blocks in every
     * direction) would be flooded by a river of the unknown dimension.
     */
    public static <T> Optional<T> rejectInRiver(Optional<T> original, Structure.GenerationContext context, int radius){
        if(original.isEmpty() || !isUnknownDimensionBiome(context)){
            return original;
        }
        int centerX = context.chunkPos().getMinBlockX();
        int centerZ = context.chunkPos().getMinBlockZ();
        return isAnyFlooded(context,centerX,centerZ,radius) ? Optional.empty() : original;
    }

    private static boolean isAnyFlooded(Structure.GenerationContext context, int centerX, int centerZ, int radius){
        for(int dx = -radius; dx <= radius; dx += CHECK_STEP){
            for(int dz = -radius; dz <= radius; dz += CHECK_STEP){
                if(isFlooded(context,centerX + dx,centerZ + dz)){
                    return true;
                }
            }
        }
        //The grid above may stop short of the outer edge, so walk it explicitly
        for(int dx = -radius; dx <= radius; dx += CHECK_STEP){
            if(isFlooded(context,centerX + dx,centerZ + radius) || isFlooded(context,centerX + dx,centerZ - radius)){
                return true;
            }
        }
        for(int dz = -radius; dz <= radius; dz += CHECK_STEP){
            if(isFlooded(context,centerX + radius,centerZ + dz) || isFlooded(context,centerX - radius,centerZ + dz)){
                return true;
            }
        }
        return false;
    }

    private CDPRiverGuard(){
    }
}
