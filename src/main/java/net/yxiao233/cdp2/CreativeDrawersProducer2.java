package net.yxiao233.cdp2;

import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.yxiao233.cdp2.common.event.CreativeModeTabEvent;
import net.yxiao233.cdp2.common.integration.arsnouveau.StructureRitualDefinition;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPRegistryEvent;
import net.yxiao233.cdp2.common.registry.*;
import org.slf4j.Logger;

@Mod(CreativeDrawersProducer2.MODID)
public class CreativeDrawersProducer2{
    public static final String MODID = "cdp2";
    public static final Logger LOGGER = LogUtils.getLogger();
    public CreativeDrawersProducer2(IEventBus modEventBus, ModContainer modContainer) {
        CDPItem.init(modEventBus);
        CDPBlock.init(modEventBus);
        CDPTab.init(modEventBus);
        CDPDataComponentTypes.DATA_COMPONENTS.register(modEventBus);
        CDPRecipe.init(modEventBus);
        modEventBus.addListener(CreativeModeTabEvent::onBuild);
    }

    @SuppressWarnings({"removal","deprecation"})
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(CDPBlock.VOID_SIEVE.getBlock(), RenderType.translucent());
        }
    }

    public static ResourceLocation makeId(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
