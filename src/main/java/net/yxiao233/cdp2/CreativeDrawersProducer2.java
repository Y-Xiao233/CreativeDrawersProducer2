package net.yxiao233.cdp2;

import appeng.api.client.AEKeyRendering;
import com.moakiee.ae2lt.packaged.logic.multiblock.AdapterRegistration;
import com.moakiee.ae2lt.packaged.logic.multiblock.MultiblockAdapterRegistry;
import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.yxiao233.cdp2.common.event.CreativeModeTabEvent;
import net.yxiao233.cdp2.common.integration.ae2.key.CDPAEKeyType;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKey;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKeyRenderHandler;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKeyType;
import net.yxiao233.cdp2.common.integration.ae2ltpp.CDPPackagedCoreBakedModel;
import net.yxiao233.cdp2.common.integration.ae2ltpp.CDPPackagedCoreItemRenderer;
import net.yxiao233.cdp2.common.integration.ae2ltpp.HephaestusForgeAdapter;
import net.yxiao233.cdp2.common.integration.ftbquests.CDPTaskTypes;
import net.yxiao233.cdp2.common.integration.kubejs.event.BlockModifyEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.BlockRegistryEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPModifyEvent;
import net.yxiao233.cdp2.common.integration.kubejs.event.CDPRegistryEvent;
import net.yxiao233.cdp2.common.registry.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.Map;

@Mod(CreativeDrawersProducer2.MODID)
public class CreativeDrawersProducer2{
    public static final String MODID = "cdp2";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static boolean frozenCreativeDrawer = false;
    private static boolean loadKubeJSWhiteList = false;
    public static boolean hideNeededAura = false;
    public CreativeDrawersProducer2(IEventBus modEventBus, ModContainer modContainer) {
        CDPItem.init(modEventBus); 
        CDPBlock.init(modEventBus);
        CDPFeature.init(modEventBus);
        CDPTab.init(modEventBus);
        CDPDataComponentTypes.DATA_COMPONENTS.register(modEventBus);
        CDPRecipe.init(modEventBus);
        modEventBus.addListener(CreativeDrawersProducer2::onRegistry);
        modEventBus.addListener(CreativeModeTabEvent::onBuild);
        modEventBus.addListener(CreativeDrawersProducer2::commonSetup);
        modEventBus.addListener(CDPAEKeyType::register);
        CDPTaskTypes.init();
    }

    public static void onRegistry(RegisterEvent event){
        if(!frozenCreativeDrawer){
            var e = new BlockRegistryEvent();
            CDPRegistryEvent.BLOCK.post(ScriptType.STARTUP, e);
            frozenCreativeDrawer = true;
        }
    }

    public static void commonSetup(FMLCommonSetupEvent event){
        if(!loadKubeJSWhiteList){
            var modifyEvent = new BlockModifyEvent();
            CDPModifyEvent.BLOCK.post(ScriptType.STARTUP, modifyEvent);
            loadKubeJSWhiteList = true;
        }

        MultiblockAdapterRegistry.register(AdapterRegistration.of(HephaestusForgeAdapter.ADAPTER_ID, new HephaestusForgeAdapter()));
    }

    @SuppressWarnings({"removal","deprecation"})
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(CDPBlock.VOID_SIEVE.getBlock(), RenderType.translucent());
            AEKeyRendering.register(ForbiddenEssenceKeyType.INSTANCE, ForbiddenEssenceKey.class, ForbiddenEssenceKeyRenderHandler.INSTANCE);
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            final CDPPackagedCoreItemRenderer renderer = new CDPPackagedCoreItemRenderer(ResourceLocation.parse("forbidden_arcanus:hephaestus_forge_tier_5"));
            event.registerItem(new IClientItemExtensions() {
                public @NotNull CDPPackagedCoreItemRenderer getCustomRenderer() {
                    return renderer;
                }
            }, CDPItem.FORGE_CORE.asItem());
        }

        @SubscribeEvent
        public static void wrapPackagedCoreItemModels(ModelEvent.ModifyBakingResult event) {
            Map<ModelResourceLocation, BakedModel> models = event.getModels();

            ModelResourceLocation key = ModelResourceLocation.inventory(BuiltInRegistries.ITEM.getKey(CDPItem.FORGE_CORE.asItem()));
            BakedModel model = models.get(key);
            if (model != null) {
                models.put(key, new CDPPackagedCoreBakedModel(model));
            }

        }
    }

    public static ResourceLocation makeId(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,path);
    }
}
