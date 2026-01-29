package net.yxiao233.cdp2.integration.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;
import net.minecraft.ChatFormatting;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;

import java.lang.reflect.Modifier;
import java.util.Arrays;

public class CDPCropTier {
    public static final CropTier SEVEN = new CropTier(CreativeDrawersProducer2.makeId("absolute"),7,16776960, ChatFormatting.YELLOW);
    public static final CropTier EIGHT = new CropTier(CreativeDrawersProducer2.makeId("supreme"),8,11141120, ChatFormatting.DARK_RED);
    public static final CropTier NINE = new CropTier(CreativeDrawersProducer2.makeId("cosmic"),9,16733695, ChatFormatting.LIGHT_PURPLE);
    public static final CropTier TEN = new CropTier(CreativeDrawersProducer2.makeId("infinite"),10,16733695, ChatFormatting.DARK_PURPLE);

    public static void onRegisterCrops(ICropRegistry registry) {
        Arrays.stream(CDPCropTier.class.getFields()).toList().forEach(field -> {
            if(Modifier.isStatic(field.getModifiers())){
                try {
                    Object object = field.get(null);
                    if(object instanceof CropTier cropTier){
                        registry.registerTier(cropTier);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public static void onPostRegisterCrops(ICropRegistry registry) {
        SEVEN.setFarmland(CDPBlock.ABSOLUTE_FARMLAND.getBlock()).setEssence(CDPItem.ABSOLUTE_ESSENCE.getItemHolder());
        EIGHT.setFarmland(CDPBlock.SUPREME_FARMLAND.getBlock()).setEssence(CDPItem.SUPREME_ESSENCE.getItemHolder());
        NINE.setFarmland(CDPBlock.COSMIC_FARMLAND.getBlock()).setEssence(CDPItem.COSMIC_ESSENCE.getItemHolder());
        TEN.setFarmland(CDPBlock.INFINITE_FARMLAND.getBlock()).setEssence(CDPItem.INFINITE_ESSENCE.getItemHolder());
    }
}
