package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.structures.jisaw.CataclysmJigsawStructure;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.yxiao233.cdp2.common.registry.CDPBiomes;
import net.yxiao233.cdp2.worldgen.CDPRiverGuard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

/**
 * Cataclysm's jigsaw structures take their start height from {@code start_height} and may project it to a heightmap.
 * The soul black smith has neither a heightmap projection nor a terrain fitting height: it is pinned to the nether
 * terrain with {@code start_height: 64}, which would leave it floating or buried in the unknown dimension.
 *
 * <p>This replaces that sampled Y with the surface height of the column, but only for the soul black smith start pool
 * inside the quorveth biome. The ancient factory (Y -30, underground) and the frosted prison (surface through its own
 * {@code project_start_to_heightmap}) are untouched.</p>
 *
 * <p>Surface jigsaw structures are also dropped when the rivers of the unknown dimension would flood them.</p>
 */
@Mixin(CataclysmJigsawStructure.class)
public abstract class CataclysmJigsawStructureMixin {
    @Unique
    private static final ResourceKey<StructureTemplatePool> cdp2$soulBlackSmithPool = ResourceKey.create(
            Registries.TEMPLATE_POOL,ResourceLocation.fromNamespaceAndPath("cataclysm","soul_black_smith/start_pool"));

    @Shadow
    @Final
    public Holder<StructureTemplatePool> startPool;

    @Shadow
    @Final
    public Optional<Heightmap.Types> projectStartToHeightmap;

    @Shadow
    @Final
    public int maxDistanceFromCenter;

    @ModifyExpressionValue(
            method = "findGenerationPoint",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/heightproviders/HeightProvider;sample(Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/WorldGenerationContext;)I"
            )
    )
    private int cdp2$surfaceStartY(int original, Structure.GenerationContext context){
        if(!this.startPool.is(cdp2$soulBlackSmithPool)){
            return original;
        }
        if(!CDPRiverGuard.biomeAt(context).is(CDPBiomes.QUORVETH)){
            return original;
        }
        return context.chunkGenerator()
                .getBaseHeight(
                        context.chunkPos().getMinBlockX(),
                        context.chunkPos().getMinBlockZ(),
                        Heightmap.Types.WORLD_SURFACE_WG,
                        context.heightAccessor(),
                        context.randomState()
                );
    }

    /** Surface jigsaw structures must not be flooded by the rivers of the unknown dimension, not even at their edge */
    @ModifyReturnValue(
            method = "findGenerationPoint",
            at = @At("RETURN")
    )
    private Optional<Structure.GenerationStub> cdp2$notInRiver(Optional<Structure.GenerationStub> original, Structure.GenerationContext context){
        if(this.projectStartToHeightmap.isEmpty() && !this.startPool.is(cdp2$soulBlackSmithPool)){
            //An underground structure (the ancient factory sits at Y -30) is not affected by the rivers
            return original;
        }
        return CDPRiverGuard.rejectInRiver(original,context,this.maxDistanceFromCenter);
    }
}
