package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.structures.Burning_Arena_Structure;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.yxiao233.cdp2.api.minecraft.ICDPStructureTemplate;
import net.yxiao233.cdp2.common.registry.CDPBiomes;
import net.yxiao233.cdp2.worldgen.CDPRiverGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * The burning arena of Cataclysm hardcodes its start height to the nether bedrock level: both
 * {@code findGenerationPoint} and {@code generatePieces} build {@code new BlockPos(x, 21, z)}.
 *
 * <p>Inside the myrkhal biome (the unknown dimension) the arena would end up buried far below the surface, so there
 * the hardcoded Y is replaced by the surface height of the column. Everything else stays untouched: the structure id,
 * the structure set (spacing/separation/salt/exclusion zone), the pieces and the other dimensions.</p>
 *
 * <p>The two lower halves of the arena ({@code burning_arena5} to {@code burning_arena8}) carry a solid layer of
 * netherrack on their highest layer, which is a leftover of the nether generation and pointless in the unknown
 * dimension (it ends up hovering right above the arena). It is removed from the in memory templates right after the
 * pieces were built, so nothing about the structure itself changes.</p>
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

    /**
     * Removes the pointless netherrack layer from the top of the templates as soon as the pieces were built (they only
     * reference the templates, the blocks are placed later), so the arena is generated without it.
     */
    @Inject(
            method = "generatePieces",
            at = @At("TAIL")
    )
    private static void cdp2$stripTopNetherrack(StructurePiecesBuilder builder, Structure.GenerationContext context, CallbackInfo ci){
        for(int i = 1; i <= 8; i++){
            StructureTemplate template = context.structureTemplateManager()
                    .getOrCreate(ResourceLocation.fromNamespaceAndPath("cataclysm","burning_arena" + i));
            ((ICDPStructureTemplate)(Object)template).cdp2$removeTopNetherrackLayer();
        }
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
