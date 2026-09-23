package net.yxiao233.cdp2.common.integration.ae2ltpp;

import com.moakiee.ae2lt.packaged.registry.PPItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public final class CDPPackagedCoreItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final float TARGET_CENTER_X = 0.78F;
    private static final float TARGET_CENTER_Y = 0.22F;
    private static final float TARGET_CENTER_Z = 0.57F;
    private static final float TARGET_SCALE = 0.45F;
    private static final float TARGET_DEPTH_SCALE = 0.02F;
    private final ResourceLocation targetItemId;

    public CDPPackagedCoreItemRenderer(ResourceLocation targetItemId) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.targetItemId = targetItemId;
    }

    public void renderByItem(@NotNull ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderBase(poseStack, bufferSource, packedLight, packedOverlay);
        targetStack(stack,targetItemId).ifPresent((target) -> renderTarget(target, poseStack, bufferSource, packedLight, packedOverlay));
    }

    private static Optional<ItemStack> targetStack(ItemStack stack, ResourceLocation targetItemId) {
        if (!BuiltInRegistries.ITEM.containsKey(targetItemId)) {
            return Optional.empty();
        }

        Item item = BuiltInRegistries.ITEM.get(targetItemId);
        if (item != Items.AIR) {
            return Optional.of(new ItemStack(item));
        }

        return Optional.empty();
    }

    private static void renderBase(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack((ItemLike) PPItems.BASIC_PACKAGED_CORE.get()), ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, bufferSource, (Level)null, 0);
        poseStack.popPose();
    }

    private static void renderTarget(ItemStack target, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.78F, 0.22F, 0.57F);
        poseStack.scale(0.45F, 0.45F, 0.009F);
        Minecraft.getInstance().getItemRenderer().renderStatic(target, ItemDisplayContext.GUI, packedLight, packedOverlay, poseStack, bufferSource, null, 0);
        poseStack.popPose();
    }
}
