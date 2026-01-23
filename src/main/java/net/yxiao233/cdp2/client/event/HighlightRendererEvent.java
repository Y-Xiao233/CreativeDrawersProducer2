package net.yxiao233.cdp2.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.client.renderer.CDPRenderTypes;
import net.yxiao233.cdp2.common.block.entity.UpgradeStationBlockEntity;
import net.yxiao233.cdp2.util.LevelUtil;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HighlightRendererEvent {
    @SubscribeEvent
    public static void onRenderer(RenderLevelStageEvent event){
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES && Minecraft.getInstance().level != null){
            UpgradeStationBlockEntity.entries.values().forEach(entry ->{
                if(entry.getLevel() == null || !LevelUtil.checkDimension(Minecraft.getInstance().level.dimension(),entry.getLevel().dimension())){
                    return;
                }
                if(!(Minecraft.getInstance().level.getBlockEntity(entry.getBlockPos()) instanceof UpgradeStationBlockEntity)){{
                    return;
                }}
                if(entry.isShowRange()){
                    BlockPos pos = entry.getBlockPos();
                    Vec3 camera = event.getCamera().getPosition().reverse();
                    AABB box = entry.getBoundary().move(camera);
                    MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                    PoseStack poseStack = event.getPoseStack();
                    poseStack.pushPose();
                    RenderSystem.disableDepthTest();
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    LevelRenderer.renderLineBox(poseStack,bufferSource.getBuffer(RenderType.lines()),box,1,1,0,0.5f);
                    for(Direction direction : Direction.values()){
                        LevelRenderer.renderFace(poseStack,bufferSource.getBuffer(CDPRenderTypes.WORK_AREA), direction,(float) box.maxX,(float) box.maxY,(float) box.maxZ,(float) box.minX,(float) box.minY,(float) box.minZ,1,1,0,0.2f);
                    }
                    RenderSystem.disableBlend();
                    RenderSystem.enableDepthTest();
                    poseStack.popPose();
                }
            });
        }
    }
}
