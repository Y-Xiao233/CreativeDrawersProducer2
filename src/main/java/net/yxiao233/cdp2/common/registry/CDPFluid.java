package net.yxiao233.cdp2.common.registry;

import com.buuz135.industrial.module.IModule;
import com.hrznstudio.titanium.fluid.ClientFluidTypeExtensions;
import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.fluid.BaseFluidInstance;

public class CDPFluid implements IModule {
    public static BaseFluidInstance LIQUID_UNKNOWN;
    @Override
    public void generateFeatures(DeferredRegistryHelper helper) {
        LIQUID_UNKNOWN = simpleFluid(helper,"liquid_unknown");
    }

    public static BaseFluidInstance simpleFluid(DeferredRegistryHelper helper, String name) {
        return new BaseFluidInstance(helper, name, net.neoforged.neoforge.fluids.FluidType.Properties.create().density(1000), new ClientFluidTypeExtensions(CreativeDrawersProducer2.makeId("block/fluids/" + name + "_still"), CreativeDrawersProducer2.makeId("block/fluids/" + name + "_flow")));
    }
}
