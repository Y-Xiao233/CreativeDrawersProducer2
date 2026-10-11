package net.yxiao233.cdp2.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.yxiao233.cdp2.common.registry.CDPFluid;

import java.util.ArrayList;
import java.util.List;

/**
 * Fills the rivers of the unknown dimension with the unknown fluid.
 *
 * <p>This runs in the {@code FLUID_SPRINGS} step, which is <b>after</b> the structures have been generated, so a
 * structure that reaches below the sea level used to end up flooded. To prevent that the surrounding structure starts
 * are collected first (their positions are already known since {@code STRUCTURE_STARTS} runs before any decoration
 * step) and no fluid is placed inside their bounding boxes.</p>
 */
public class CDPRiverFluidFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEA_LEVEL = 40;
    /** The largest structures reach roughly 256 blocks, so the eight surrounding chunks are inspected */
    private static final int STRUCTURE_CHUNK_RADIUS = 8;
    /**
     * Keep that much space around a structure free of fluid. The fluid is placed as a real liquid block, so once the
     * chunks are loaded it would flow into the structure through any opening if it were placed right next to it.
     */
    private static final int STRUCTURE_MARGIN = 8;

    public CDPRiverFluidFeature(){
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context){
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        BlockState gas = CDPFluid.LIQUID_UNKNOWN.getSourceFluid().get().defaultFluidState().createLegacyBlock();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        List<BoundingBox> structures = cdp2$structuresAround(level,chunkPos);

        for(int dx = 0; dx < 16; dx++){
            for(int dz = 0; dz < 16; dz++){
                int x = chunkPos.getMinBlockX() + dx;
                int z = chunkPos.getMinBlockZ() + dz;
                int y = SEA_LEVEL - 1;
                while(y > level.getMinBuildHeight() && level.getBlockState(pos.set(x,y,z)).isAir()){
                    y--;
                }
                for(int fill = y + 1; fill < SEA_LEVEL; fill++){
                    if(cdp2$insideStructure(structures,x,fill,z)){
                        //A structure occupies this spot, the river must not flood it
                        continue;
                    }
                    level.setBlock(pos.set(x,fill,z),gas,2);
                }
            }
        }

        return true;
    }

    /** Every structure start that may reach into the given chunk */
    private static List<BoundingBox> cdp2$structuresAround(WorldGenLevel level, ChunkPos center){
        List<BoundingBox> boxes = new ArrayList<>();
        for(int cx = center.x - STRUCTURE_CHUNK_RADIUS; cx <= center.x + STRUCTURE_CHUNK_RADIUS; cx++){
            for(int cz = center.z - STRUCTURE_CHUNK_RADIUS; cz <= center.z + STRUCTURE_CHUNK_RADIUS; cz++){
                ChunkAccess chunk = level.getChunk(cx,cz,ChunkStatus.STRUCTURE_STARTS,false);
                if(chunk == null){
                    continue;
                }
                for(StructureStart start : chunk.getAllStarts().values()){
                    if(start.isValid()){
                        boxes.add(start.getBoundingBox().inflatedBy(STRUCTURE_MARGIN));
                    }
                }
            }
        }
        return boxes;
    }

    private static boolean cdp2$insideStructure(List<BoundingBox> boxes, int x, int y, int z){
        for(BoundingBox box : boxes){
            if(box.isInside(x,y,z)){
                return true;
            }
        }
        return false;
    }
}
