package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.structures.Burning_Arena_Structure;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.yxiao233.cdp2.common.registry.CDPBiomes;
import net.yxiao233.cdp2.worldgen.CDPRiverGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

/**
 * The burning arena of Cataclysm hardcodes its start height to the nether bedrock level: both
 * {@code findGenerationPoint} and {@code generatePieces} build {@code new BlockPos(x, 21, z)}.
 *
 * <p>Inside the myrkhal biome (the unknown dimension) the arena would end up buried far below the surface, so there
 * the hardcoded Y is replaced by the surface height of the column. Everything else stays untouched: the structure id,
 * the structure set (spacing/separation/salt/exclusion zone), the pieces and the other dimensions.</p>
 */
@Mixin(Burning_Arena_Structure.class)
public abstract class BurningArenaStructureMixin {
    /** The arena is larger than a hundred blocks across, so its whole footprint has to stay out of the rivers */
    @Unique
    private static final int cdp2$footprintRadius = 64;

    @ModifyExpressionValue(
            method = "findGenerationPoint",
            at = @At(value = "CONSTANT", args = "intValue=21")
    )
    private int cdp2$arenaStartY(int original, Structure.GenerationContext context){
        return cdp2$surfaceY(original,context);
    }

    @ModifyExpressionValue(
            method = "generatePieces",
            at = @At(value = "CONSTANT", args = "intValue=21")
    )
    private static int cdp2$arenaPieceY(int original, StructurePiecesBuilder builder, Structure.GenerationContext context){
        return cdp2$surfaceY(original,context);
    }

    /** The arena is a surface structure, so a river must not be anywhere near it */
    @ModifyReturnValue(
            method = "findGenerationPoint",
            at = @At("RETURN")
    )
    private Optional<Structure.GenerationStub> cdp2$notInRiver(Optional<Structure.GenerationStub> original, Structure.GenerationContext context){
        return CDPRiverGuard.rejectInRiver(original,context,cdp2$footprintRadius);
    }

    @Unique
    private static int cdp2$surfaceY(int original, Structure.GenerationContext context){
        if(!CDPRiverGuard.biomeAt(context).is(CDPBiomes.MYRKHAL)){
            return original;
        }
        int surface = context.chunkGenerator()
                .getBaseHeight(
                        context.chunkPos().getMinBlockX(),
                        context.chunkPos().getMinBlockZ(),
                        Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(),
                        context.randomState()
                );
        //The whole arena is about 100 blocks tall, keep it inside the build height of the dimension
        return Math.min(surface,context.heightAccessor().getMaxBuildHeight() - 100);
    }
}
