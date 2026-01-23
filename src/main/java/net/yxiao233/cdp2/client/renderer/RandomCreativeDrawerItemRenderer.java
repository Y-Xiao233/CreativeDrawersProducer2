package net.yxiao233.cdp2.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.api.renderer.CDPItemRenderer;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.util.RenderUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RandomCreativeDrawerItemRenderer extends CDPItemRenderer {
    private int time;
    private final int idle = 40;
    private final HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<?>> drawersMap;
    public RandomCreativeDrawerItemRenderer(HashMap<ResourceLocation, CDPBlockEntityDeferredRegister<?>> drawersMap){
        this.drawersMap = drawersMap;
    }

    @SuppressWarnings("all")
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderUtil.renderBlockItem(drawersMap.values().stream().toList().getFirst().asItem().getDefaultInstance(), displayContext, poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, (pose) -> {
            pose.mulPose(Axis.YP.rotationDegrees(180));
            pose.mulPose(Axis.XN.rotationDegrees(90));
            pose.mulPose(Axis.YP.rotationDegrees(90));
        });

        poseStack.pushPose();

        poseStack.translate(0.5f,0.5f,0.5f);
        poseStack.mulPose(Axis.XN.rotationDegrees(180));
        poseStack.mulPose(Axis.ZN.rotationDegrees(270));
        poseStack.translate(0,0,0.42f);

        time ++;
        int index = time / idle;
        if(index > drawersMap.size() - 1){
            time = 0;
            index = 0;
        }
        CDPBlockEntityDeferredRegister<?> register = drawersMap.values().stream().toList().get(index);
        ItemStack infinityItem = null;
        if(register.asBlock() instanceof CreativeDrawerBlock drawerBlock){
            infinityItem = drawerBlock.getInfinityItem().get();
        }

        if(infinityItem != null){
            if(infinityItem.getItem() instanceof BlockItem){
                poseStack.translate(0,0,-0.1f);
            }
            poseStack.scale(0.6f,0.6f,0.6f);
            Minecraft.getInstance().getItemRenderer().renderStatic(infinityItem,ItemDisplayContext.FIXED,packedLight,packedOverlay,poseStack,buffer,Minecraft.getInstance().level,0);

            if(infinityItem.getItem() instanceof BlockItem){
                poseStack.translate(0,0,0.16f);
            }
            poseStack.translate(0,0.1f,0.05f);
            RenderUtil.renderText(poseStack,buffer,packedLight, Component.literal("2.14G"),0.6f);
        }


        poseStack.popPose();
    }
}
