package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), CDPBlockLootTablesProvider.create(packOutput,lookupProvider));
        generator.addProvider(event.includeClient(), new CDPBlockStateProvider(packOutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new CDPItemModelProvider(packOutput,existingFileHelper));
        CDPBlockTagProvider blockTagProvider = generator.addProvider(event.includeClient(),new CDPBlockTagProvider(packOutput,lookupProvider,existingFileHelper));
        generator.addProvider(event.includeClient(), new CDPItemTagProvider(packOutput,lookupProvider,blockTagProvider.contentsGetter()));
    }
}
