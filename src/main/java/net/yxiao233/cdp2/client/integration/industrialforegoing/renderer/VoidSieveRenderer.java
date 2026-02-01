package net.yxiao233.cdp2.client.integration.industrialforegoing.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.renderer.CDPBaseBlockEntityRenderer;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.VoidSieveBlockEntity;
import net.yxiao233.cdp2.util.RenderUtil;
import org.jetbrains.annotations.NotNull;

public class VoidSieveRenderer extends CDPBaseBlockEntityRenderer<VoidSieveBlockEntity> {
    public VoidSieveRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull VoidSieveBlockEntity entity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        if(entity.shouldDisplay()){
            ItemStack blockForDisplay = entity.getBlockForDisplay();
            BlockState state = Block.byItem(blockForDisplay.getItem()).defaultBlockState();
            int progress = entity.progress;
            int maxProgress = entity.getProgressBar().getMaxProgress();
//            if(progress >= maxProgress || !entity.canIncrease()){
//                return;
//            }
            double yPos = 1 - ((double) progress / maxProgress);
            RenderUtil.renderBlock(state,entity.getBlockPos(),poseStack,multiBufferSource,stack ->{
                stack.scale(0.76f,0.8f,0.76f);
                stack.translate(0.15f,yPos,0.15f);
            });
        }
    }
}
