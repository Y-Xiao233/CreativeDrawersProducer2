package net.yxiao233.cdp2.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.yxiao233.cdp2.api.registry.CDPPlacedFeatureRegister;
import net.yxiao233.cdp2.common.registry.CDPBaseFeature;

public class CDPPlacedFeatures {
    public static void bootstrap(BootstrapContext<PlacedFeature> context){
        CDPBaseFeature.init();
        CDPPlacedFeatureRegister.registry(context);
    }
}
