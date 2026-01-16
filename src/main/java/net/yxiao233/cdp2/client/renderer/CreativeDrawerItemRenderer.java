package net.yxiao233.cdp2.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.yxiao233.cdp2.api.renderer.CDPItemRenderer;
import net.yxiao233.cdp2.util.RendererUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class CreativeDrawerItemRenderer extends CDPItemRenderer {
    private final Supplier<ItemStack> infinityItem;
    public CreativeDrawerItemRenderer(Supplier<ItemStack> infinityItem) {
        this.infinityItem = infinityItem;
    }
    @Override
    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RendererUtil.renderBlockItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay, ModelData.EMPTY, (pose) -> {
            pose.mulPose(Axis.YP.rotationDegrees(180));
            pose.mulPose(Axis.YP.rotationDegrees(90));
        });

        poseStack.pushPose();

        poseStack.translate(0.5f,0.5f,0.5f);
        poseStack.mulPose(Axis.XN.rotationDegrees(180));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.translate(0,0,0.42f);
        if(infinityItem.get().getItem() instanceof BlockItem){
            poseStack.translate(0,0,-0.1f);
        }
        poseStack.scale(0.6f,0.6f,0.6f);
        Minecraft.getInstance().getItemRenderer().renderStatic(infinityItem.get(),ItemDisplayContext.FIXED,packedLight,packedOverlay,poseStack,buffer,Minecraft.getInstance().level,0);

        if(infinityItem.get().getItem() instanceof BlockItem){
            poseStack.translate(0,0,0.16f);
        }
        poseStack.translate(0,0.1f,0.05f);
        RendererUtil.renderText(poseStack,buffer,packedLight,Component.literal("2.14G"),0.6f);

        poseStack.popPose();
    }
}
