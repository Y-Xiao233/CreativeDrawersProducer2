package net.yxiao233.cdp2.common.integration.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.IMysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.MysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;

@MysticalAgriculturePlugin
public class CDPMysticalAgriculturePlugin implements IMysticalAgriculturePlugin {
    @Override
    public void onRegisterCrops(ICropRegistry registry) {
        CDPCropTier.onRegisterCropTiers(registry);
        CDPCrops.onRegisterCrops(registry);
    }

    @Override
    public void onPostRegisterCrops(ICropRegistry registry) {
        CDPCropTier.onPostRegisterCropTiers(registry);
        CDPCrops.onPostRegisterCrops(registry);
    }
}
