package net.yxiao233.cdp2.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.yxiao233.cdp2.api.registry.CDPBiomeModifiersRegister;
import net.yxiao233.cdp2.api.registry.CDPSpawnBiomeModifiersRegister;
import net.yxiao233.cdp2.common.registry.CDPBaseFeature;
import net.yxiao233.cdp2.common.registry.CDPSpawns;

public class CDPBiomeModifiers {
    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        CDPBaseFeature.init();
        CDPSpawns.init();
        CDPBiomeModifiersRegister.registry(context);
        CDPSpawnBiomeModifiersRegister.registry(context);
    }
}
