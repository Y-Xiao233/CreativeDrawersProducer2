package net.yxiao233.cdp2.common.integration.ae2.key;

import appeng.api.client.AEKeyRenderHandler;
import appeng.client.gui.style.Blitter;
import com.moakiee.ae2lt.me.key.LightningKey;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import org.joml.Matrix4f;

public class ForbiddenEssenceKeyRenderHandler implements AEKeyRenderHandler<ForbiddenEssenceKey> {
    public static final ForbiddenEssenceKeyRenderHandler INSTANCE = new ForbiddenEssenceKeyRenderHandler();
    private static final ResourceLocation BLOOD = CreativeDrawersProducer2.makeId("item/blood");
    private static final ResourceLocation AUREAL = CreativeDrawersProducer2.makeId("item/aureal");
    private static final ResourceLocation SOULS = CreativeDrawersProducer2.makeId("item/souls");
    private static final ResourceLocation EXPERIENCE = CreativeDrawersProducer2.makeId("item/experience");

    public static TextureAtlasSprite spriteFor(ForbiddenEssenceKey stack) {
        ResourceLocation id = switch (stack.type()){
            case EXPERIENCE -> EXPERIENCE;
            case BLOOD -> BLOOD;
            case AUREAL -> AUREAL;
            case SOULS -> SOULS;
        };
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(id);
    }
    @Override
    public void drawInGui(Minecraft minecraft, GuiGraphics guiGraphics, int x, int y, ForbiddenEssenceKey stack) {
        Blitter.sprite(spriteFor(stack)).dest(x, y, 16, 16).blit(guiGraphics);
    }

    @Override
    public void drawOnBlockFace(PoseStack poseStack, MultiBufferSource buffers, ForbiddenEssenceKey what, float scale, int combinedLight, Level level) {
        TextureAtlasSprite sprite = spriteFor(what);
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.01F);
        VertexConsumer buffer = buffers.getBuffer(RenderType.cutout());
        scale -= 0.05F;
        float x0 = -scale / 2.0F;
        float y0 = scale / 2.0F;
        float x1 = scale / 2.0F;
        float y1 = -scale / 2.0F;
        Matrix4f transform = poseStack.last().pose();
        buffer.addVertex(transform, x0, y1, 0.0F).setColor(-1).setUv(sprite.getU0(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLight).setNormal(0.0F, 0.0F, 1.0F);
        buffer.addVertex(transform, x1, y1, 0.0F).setColor(-1).setUv(sprite.getU1(), sprite.getV1()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLight).setNormal(0.0F, 0.0F, 1.0F);
        buffer.addVertex(transform, x1, y0, 0.0F).setColor(-1).setUv(sprite.getU1(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLight).setNormal(0.0F, 0.0F, 1.0F);
        buffer.addVertex(transform, x0, y0, 0.0F).setColor(-1).setUv(sprite.getU0(), sprite.getV0()).setOverlay(OverlayTexture.NO_OVERLAY).setLight(combinedLight).setNormal(0.0F, 0.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public Component getDisplayName(ForbiddenEssenceKey stack) {
        return stack.getDisplayName();
    }
}
