package net.yxiao233.cdp2.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.cdp2.api.block.property.RotationHandler;
import net.yxiao233.cdp2.api.renderer.CDPBaseBlockEntityRenderer;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.util.RendererUtil;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CreativeDrawerRenderer extends CDPBaseBlockEntityRenderer<CreativeDrawerBlockEntity> {


    public CreativeDrawerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@NotNull CreativeDrawerBlockEntity entity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        ItemStack stack = entity.getInfinityItem();
        BlockState state = entity.getBlockState();
        if(stack == null || !state.hasProperty(RotationHandler.FACING_HORIZONTAL)){
            return;
        }
        poseStack.pushPose();

        Direction direction = state.getValue(RotationHandler.FACING_HORIZONTAL);
        poseStack.translate(0.5f,0.5f,0.5f);
        switch (direction){
            case NORTH -> {
                poseStack.mulPose(Axis.XN.rotationDegrees(180));
                poseStack.mulPose(Axis.ZN.rotationDegrees(180));
            }
            case SOUTH -> {
            }
            case WEST -> poseStack.mulPose(Axis.YN.rotationDegrees(90));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
        }
        poseStack.translate(0,0,0.42f);
        if(stack.getItem() instanceof BlockItem){
            poseStack.translate(0,0,-0.1f);
        }
        poseStack.scale(0.6f,0.6f,0.6f);
        renderStaticItem(stack,entity,poseStack,multiBufferSource,direction);

        if(stack.getItem() instanceof BlockItem){
            poseStack.translate(0,0,0.16f);
        }
        poseStack.translate(0,0.1f,0.05f);
        RendererUtil.renderText(poseStack,multiBufferSource,combinedLight, Component.literal("2.14G"),0.6f);

        poseStack.popPose();
    }
}
