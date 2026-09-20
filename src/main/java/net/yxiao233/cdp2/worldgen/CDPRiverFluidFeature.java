package net.yxiao233.cdp2.worldgen;

import com.buuz135.industrial.module.ModuleCore;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CDPRiverFluidFeature extends Feature<NoneFeatureConfiguration> {
    private static final int SEA_LEVEL = 40;

    public CDPRiverFluidFeature(){
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context){
        WorldGenLevel level = context.level();
        ChunkPos chunkPos = new ChunkPos(context.origin());
        BlockState gas = ModuleCore.ETHER.getSourceFluid().get().defaultFluidState().createLegacyBlock();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int dx = 0; dx < 16; dx++){
            for(int dz = 0; dz < 16; dz++){
                int x = chunkPos.getMinBlockX() + dx;
                int z = chunkPos.getMinBlockZ() + dz;
                int y = SEA_LEVEL - 1;
                while(y > level.getMinBuildHeight() && level.getBlockState(pos.set(x,y,z)).isAir()){
                    y--;
                }
                for(int fill = y + 1; fill < SEA_LEVEL; fill++){
                    level.setBlock(pos.set(x,fill,z),gas,2);
                }
            }
        }

        return true;
    }
}
