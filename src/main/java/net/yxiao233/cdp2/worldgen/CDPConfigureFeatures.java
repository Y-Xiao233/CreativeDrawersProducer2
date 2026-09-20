package net.yxiao233.cdp2.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.yxiao233.cdp2.api.registry.CDPConfigureFeatureRegister;
import net.yxiao233.cdp2.common.registry.CDPBaseFeature;

public class CDPConfigureFeatures {
    public static void boostrap(BootstrapContext<ConfiguredFeature<?,?>> context){
        CDPBaseFeature.init();
        CDPConfigureFeatureRegister.registry(context);
        CDPTreeFeatures.bootstrapConfigured(context);
        CDPRiverFeatures.bootstrapConfigured(context);
    }
}
