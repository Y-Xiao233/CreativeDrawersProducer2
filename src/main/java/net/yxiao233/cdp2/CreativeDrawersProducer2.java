package net.yxiao233.cdp2;

import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import com.hollingsworth.arsnouveau.api.registry.RitualRegistry;
import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import com.hollingsworth.arsnouveau.common.items.RitualTablet;
import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.script.ScriptType;
import io.github.lounode.ae2cs.common.init.AECSBlocks;
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
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
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
        modEventBus.addListener(CreativeDrawersProducer2::commonSetup);
        modEventBus.addListener(CreativeModeTabEvent::onBuild);
    }

    public static void commonSetup(FMLCommonSetupEvent event){
        Upgrades.add(AEItems.SPEED_CARD, AECSBlocks.CRYSTAL_GROWTH_CHAMBER_BLOCK, 8);
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
