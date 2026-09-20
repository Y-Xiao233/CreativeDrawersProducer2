package net.yxiao233.cdp2.common.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.yxiao233.cdp2.api.registry.CDPDimensionRegister;
import net.yxiao233.cdp2.worldgen.CDPNoiseSettings;

import java.util.List;

public class CDPDimension {

    public static final CDPDimensionRegister UNKNOWN = CDPDimensionRegister.registryNoise(
            "unknown",
            List.of(
                    Pair.of(Climate.parameters(
                            Climate.Parameter.span(-1.0f,1.0f),
                            Climate.Parameter.span(-1.0f,1.0f),
                            Climate.Parameter.span(-1.0f,1.0f),
                            Climate.Parameter.span(-1.5f,-0.25f),
                            Climate.Parameter.span(-1.0f,1.0f),
                            Climate.Parameter.span(-1.0f,1.0f),
                            0.0f), CDPBiomes.MYRKHAL),
                    Pair.of(CDPDimensionRegister.allClimate(0.1f), CDPBiomes.QUORVETH)
            ),
            CDPNoiseSettings.UNKNOWN
    );

    public static void bootstrapType(BootstrapContext<DimensionType> context){
        CDPDimensionRegister.registryType(context);
    }

    public static void bootstrapStem(BootstrapContext<LevelStem> context){
        CDPDimensionRegister.registryLevelStem(context);
    }
}
