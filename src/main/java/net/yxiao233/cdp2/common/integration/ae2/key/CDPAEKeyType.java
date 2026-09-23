package net.yxiao233.cdp2.common.integration.ae2.key;

import appeng.api.stacks.AEKeyType;
import appeng.api.stacks.AEKeyTypes;
import net.neoforged.neoforge.registries.RegisterEvent;

public class CDPAEKeyType {
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(AEKeyType.REGISTRY_KEY)) {
            AEKeyTypes.register(ForbiddenEssenceKeyType.INSTANCE);
        }
    }
}
