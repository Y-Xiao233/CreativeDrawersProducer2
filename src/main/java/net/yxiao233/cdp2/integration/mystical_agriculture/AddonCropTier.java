package net.yxiao233.cdp2.integration.mystical_agriculture;

import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;
import net.minecraft.ChatFormatting;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;

public class AddonCropTier {
    public static final CropTier SEVEN = new CropTier(CreativeDrawersProducer2.makeId("7"),7,16776960, ChatFormatting.YELLOW);

    public static void onRegisterCrops(ICropRegistry registry) {
        registry.registerTier(SEVEN);
    }
    public static void onPostRegisterCrops(ICropRegistry registry) {
        SEVEN.setFarmland(CDPBlock.ABSOLUTE_FARMLAND.getBlock()).setEssence(CDPItem.ABSOLUTE_ESSENCE.getItemHolder());
    }
}
