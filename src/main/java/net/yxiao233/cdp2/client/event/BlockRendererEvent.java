package net.yxiao233.cdp2.client.event;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import net.darkhax.botanypots.common.impl.block.BotanyPotRenderer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.client.integration.arsnouveau.renderer.RitualBrazierTileRenderer;
import net.yxiao233.cdp2.client.integration.industrialforegoing.renderer.VoidSieveRenderer;
import net.yxiao233.cdp2.client.renderer.CreativeDrawerRenderer;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.VoidSieveBlockEntity;
import net.yxiao233.cdp2.common.registry.CDPBlock;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BlockRendererEvent {
    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public static void registerBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(register -> {
            event.registerBlockEntityRenderer((BlockEntityType<? extends CreativeDrawerBlockEntity>) register.asBlockEntityType(), CreativeDrawerRenderer::new);
        });
        
        CDPBlock.POTS_MAP.values().forEach(register -> {
            event.registerBlockEntityRenderer(register.asBlockEntityType(), BotanyPotRenderer::new);
        });

        event.registerBlockEntityRenderer((BlockEntityType<? extends VoidSieveBlockEntity>) CDPBlock.VOID_SIEVE.type().get(), VoidSieveRenderer::new);
        event.registerBlockEntityRenderer(BlockRegistry.RITUAL_TILE.get(), RitualBrazierTileRenderer::new);
    }
}
