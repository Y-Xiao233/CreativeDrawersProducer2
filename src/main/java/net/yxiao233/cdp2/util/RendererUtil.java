package net.yxiao233.cdp2.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class RendererUtil {
    public static void renderBlockItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext displayContext, @NotNull PoseStack matrix, @NotNull MultiBufferSource renderer, int light, int overlayLight, ModelData modelData, Consumer<PoseStack> consumerRotation) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return;
        }
        Block block = blockItem.getBlock();
        boolean fabulous;
        if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson()) {
            fabulous = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
        } else {
            fabulous = true;
        }

        matrix.popPose();
        matrix.pushPose();

        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        BlockState defaultState = block.defaultBlockState();
        BakedModel mainModel = minecraft.getModelManager().getBlockModelShaper().getBlockModel(defaultState);
        mainModel = mainModel.applyTransform(displayContext, matrix, isLeftHand(displayContext));
        consumerRotation.accept(matrix);
        matrix.translate(-.5, -.5, -.5);
        long seed = 42;
        RandomSource random = RandomSource.create();
        boolean glint = stack.hasFoil();
        for (BakedModel model : mainModel.getRenderPasses(stack, fabulous)) {
            for (RenderType renderType : model.getRenderTypes(stack, fabulous)) {
                VertexConsumer consumer = null;
                if (fabulous) {
                    consumer = ItemRenderer.getFoilBufferDirect(renderer, renderType, true, glint);
                } else {
                    consumer = ItemRenderer.getFoilBuffer(renderer, renderType, true, glint);
                }
                for (Direction direction : Direction.values()) {
                    random.setSeed(seed);
                    itemRenderer.renderQuadList(matrix, consumer, model.getQuads(defaultState, direction, random, modelData, null), stack, light, overlayLight);
                }
                random.setSeed(seed);
                itemRenderer.renderQuadList(matrix, consumer, model.getQuads(defaultState, null, random, modelData, null), stack, light, overlayLight);
            }
        }
    }

    private static boolean isLeftHand(ItemDisplayContext type){
        return type == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || type == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
    }

    public static void renderText(PoseStack matrix, MultiBufferSource renderer, int overlayLight, Component text, float maxScale) {

        matrix.translate(0, -0.745, 0.01);


        float displayWidth = 1;
        float displayHeight = 1;

        Font font = Minecraft.getInstance().font;

        int requiredWidth = Math.max(font.width(text), 1);
        int requiredHeight = font.lineHeight + 2;
        float scaler = 0.4F;
        float scaleX = displayWidth / requiredWidth;
        float scale = scaleX * scaler;
        if (maxScale > 0) {
            scale = Math.min(scale, maxScale);
        }

        matrix.scale(scale, -scale, scale);
        int realHeight = (int) Math.floor(displayHeight / scale);
        int realWidth = (int) Math.floor(displayWidth / scale);
        int offsetX = (realWidth - requiredWidth) / 2;
        int offsetY = (realHeight - requiredHeight) / 2;
        font.drawInBatch(text, offsetX - realWidth / 2, 3 + offsetY - realHeight / 2, 16777215, false, matrix.last().pose(), renderer, Font.DisplayMode.NORMAL,  0, 0xF000F0);
    }

    public static void renderSizeLabel(GuiGraphics guiGraphics, Font fontRenderer, float xPos, float yPos, String text, boolean largeFonts) {
        float scaleFactor = largeFonts ? 0.85F : 0.666F;
        PoseStack stack = guiGraphics.pose();
        stack.pushPose();
        stack.translate(0.0F, 0.0F, 200.0F);
        stack.scale(scaleFactor, scaleFactor, scaleFactor);
        renderSizeLabel(stack.last().pose(), fontRenderer, xPos, yPos, text, largeFonts);
        stack.popPose();
    }

    private static void renderSizeLabel(Matrix4f matrix, Font fontRenderer, float xPos, float yPos, String text, boolean largeFonts) {
        float scaleFactor = largeFonts ? 0.85F : 0.666F;
        float inverseScaleFactor = 1.0F / scaleFactor;
        int offset = largeFonts ? 0 : -1;
        RenderSystem.disableBlend();
        int X = (int)((xPos + (float)offset + 16.0F + 2.0F - (float)fontRenderer.width(text) * scaleFactor) * inverseScaleFactor);
        int Y = (int)((yPos + (float)offset + 16.0F - 5.0F * scaleFactor) * inverseScaleFactor);
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        fontRenderer.drawInBatch(text, (float)(X + 1), (float)(Y + 1), 4276052, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        fontRenderer.drawInBatch(text, (float)X, (float)Y, 16777215, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        buffer.endBatch();
        RenderSystem.enableBlend();
    }
}
