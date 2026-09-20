package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.worldgen.CDPRiverFluidFeature;

public class CDPFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, CreativeDrawersProducer2.MODID);
    public static final DeferredHolder<Feature<?>,CDPRiverFluidFeature> RIVER_FLUID = FEATURES.register("river_fluid",CDPRiverFluidFeature::new);

    public static void init(IEventBus eventBus){
        FEATURES.register(eventBus);
    }
}
