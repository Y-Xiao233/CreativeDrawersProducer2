package net.yxiao233.cdp2.api.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CDPBaseBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    public CDPBaseBlockEntityRenderer(BlockEntityRendererProvider.Context context){
    }


    protected void renderStaticItem(ItemStack itemStack, T entity, PoseStack poseStack, MultiBufferSource multiBufferSource, Direction direction){
        if(entity.getLevel() == null){
            return;
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos().relative(direction)),OverlayTexture.NO_OVERLAY,poseStack,multiBufferSource,entity.getLevel(),1);
    }

    @Override
    public void render(@NotNull T entity, float v, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
    }

    @Override
    public boolean shouldRender(@NotNull T blockEntity, @NotNull Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull T blockEntity) {
        return true;
    }
}
