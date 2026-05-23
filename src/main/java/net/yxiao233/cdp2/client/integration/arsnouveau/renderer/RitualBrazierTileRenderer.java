package net.yxiao233.cdp2.client.integration.arsnouveau.renderer;

import com.hollingsworth.arsnouveau.common.block.tile.RitualBrazierTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.renderer.CDPBaseBlockEntityRenderer;
import net.yxiao233.cdp2.api.structure.NbtFile;
import net.yxiao233.cdp2.client.renderer.CDPRenderTypes;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitual;
import net.yxiao233.cdp2.util.RenderUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RitualBrazierTileRenderer extends CDPBaseBlockEntityRenderer<RitualBrazierTile> {
    private NbtFile CACHE;
    public RitualBrazierTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull RitualBrazierTile entity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        if(entity.ritual instanceof StructureRitual ritual){
            NbtFile file = getNbtFile(ritual);
            List<Pair<BlockPos, BlockState>> blocks = file.getBlocks();
            blocks.forEach(info ->{
                RenderUtil.renderBlock(info.getSecond(),entity.getBlockPos(),poseStack,multiBufferSource, CDPRenderTypes.GHOST, back ->{
                    BlockPos offset = info.getFirst();
                    back.translate(ritual.xOffset,ritual.yOffset,ritual.zOffset);
                    back.translate(offset.getX(),offset.getY(),offset.getZ());
                });
            });
        }
    }


    private NbtFile getNbtFile(StructureRitual ritual){
        if(CACHE == null){
            CACHE = new NbtFile("cdp2",ritual.nbtPath);
        }
        return CACHE;
    }
}
