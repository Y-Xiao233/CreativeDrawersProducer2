package net.yxiao233.cdp2.integration.mystical_agriculture;

import com.blakebr0.mysticalagriculture.api.IMysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.MysticalAgriculturePlugin;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;

@MysticalAgriculturePlugin
public class CDPMysticalAgriculturePlugin implements IMysticalAgriculturePlugin {
    @Override
    public void onRegisterCrops(ICropRegistry registry) {
        AddonCropTier.onRegisterCrops(registry);
    }

    @Override
    public void onPostRegisterCrops(ICropRegistry registry) {
        AddonCropTier.onPostRegisterCrops(registry);
    }
}
