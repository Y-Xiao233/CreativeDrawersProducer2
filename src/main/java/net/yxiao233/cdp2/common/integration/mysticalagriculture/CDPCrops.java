package net.yxiao233.cdp2.common.integration.mysticalagriculture;

import com.blakebr0.mysticalagradditions.lib.ModCropTiers;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.CropTier;
import com.blakebr0.mysticalagriculture.api.crop.CropType;
import com.blakebr0.mysticalagriculture.api.lib.LazyIngredient;
import com.blakebr0.mysticalagriculture.api.registry.ICropRegistry;
import com.blakebr0.mysticalagriculture.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.CropBlock;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPBlock;

import java.util.function.Supplier;

public class CDPCrops {
    public static final Crop PRUDENTIUM = new Crop(rl("prudentium"), CropTier.TWO, CropType.RESOURCE, LazyIngredient.item("mysticalagriculture:prudentium_essence"));
    public static final Crop TERTIUM = new Crop(rl("tertium"), CropTier.THREE, CropType.RESOURCE, LazyIngredient.item("mysticalagriculture:prudentium_essence"));
    public static final Crop IMPERIUM = new Crop(rl("imperium"), CropTier.FOUR, CropType.RESOURCE, LazyIngredient.item("mysticalagriculture:imperium_essence"));
    public static final Crop SUPREMIUM = new Crop(rl("supremium"), CropTier.FIVE, CropType.RESOURCE, LazyIngredient.item("mysticalagriculture:supremium_essence"));
    public static final Crop INSANIUM = new Crop(rl("insanium"), ModCropTiers.SIX, CropType.RESOURCE, LazyIngredient.item("mysticalagradditions:insanium_essence"));
    public static void onRegisterCrops(ICropRegistry registry){
        processCrop(registry,PRUDENTIUM,ModItems.PRUDENTIUM_ESSENCE,() -> (CropBlock) CDPBlock.PRUDENTIUM_CROP.asBlock());
        processCrop(registry,TERTIUM,ModItems.TERTIUM_ESSENCE,() -> (CropBlock) CDPBlock.TERTIUM_CROP.asBlock());
        processCrop(registry,IMPERIUM,ModItems.IMPERIUM_ESSENCE,() -> (CropBlock) CDPBlock.IMPERIUM_CROP.asBlock());
        processCrop(registry,SUPREMIUM,ModItems.SUPREMIUM_ESSENCE,() -> (CropBlock) CDPBlock.SUPREMIUM_CROP.asBlock());
        processCrop(registry,INSANIUM, com.blakebr0.mysticalagradditions.init.ModItems.INSANIUM_ESSENCE,() -> (CropBlock) CDPBlock.INSANIUM_CROP.asBlock());
    }

    public static void onPostRegisterCrops(ICropRegistry registry){

    }


    private static ResourceLocation rl(String name){
        return CreativeDrawersProducer2.makeId(name);
    }

    private static void processCrop(ICropRegistry registry, Crop crop, Supplier<? extends Item> essenceItem, Supplier<? extends CropBlock> cropBlock){
        crop.getRecipeConfig().setSeedCraftingRecipeEnabled(false).setSeedInfusionRecipeEnabled(false);
        crop.setEssenceItem(essenceItem).setCropBlock(cropBlock);
        registry.register(crop);
    }
}
