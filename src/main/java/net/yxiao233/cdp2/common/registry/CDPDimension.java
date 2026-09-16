package net.yxiao233.cdp2.common.registry;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.yxiao233.cdp2.api.registry.CDPDimensionRegister;
import net.yxiao233.cdp2.worldgen.CDPNoiseSettings;

public class CDPDimension {

    public static final CDPDimensionRegister UNKNOWN = CDPDimensionRegister.registryNoise("unknown", CDPBiomes.UNKNOWN, CDPNoiseSettings.UNKNOWN);

    public static void bootstrapType(BootstrapContext<DimensionType> context){
        CDPDimensionRegister.registryType(context);
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context){
        CDPDimensionRegister.registryLevelStem(context);
    }
}
