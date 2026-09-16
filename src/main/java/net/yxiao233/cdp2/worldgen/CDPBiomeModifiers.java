package net.yxiao233.cdp2.worldgen;

import net.minecraft.data.worldgen.BootstrapContext;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.yxiao233.cdp2.api.registry.CDPBiomeModifiersRegister;
import net.yxiao233.cdp2.common.registry.CDPBaseFeature;

public class CDPBiomeModifiers {
    public static void bootstrap(BootstrapContext<BiomeModifier> context){
        CDPBaseFeature.init();
        CDPBiomeModifiersRegister.registry(context);
    }
}
