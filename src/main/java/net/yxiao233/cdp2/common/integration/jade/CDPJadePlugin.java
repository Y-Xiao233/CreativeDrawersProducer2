package net.yxiao233.cdp2.common.integration.jade;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPEssenceCropBlock;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPTooltips;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

@WailaPlugin
public class CDPJadePlugin implements IWailaPlugin {
    private static final ResourceLocation CDP_ESSENCE_CROP_BLOCK = CreativeDrawersProducer2.makeId("cdp_essence_crop_block");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new IBlockComponentProvider() {
            public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
                Block block = accessor.getBlock();
                Crop crop = ((ICropProvider)block).getCrop();
                BlockPos downPos = accessor.getPosition().below();
                Block belowBlock = accessor.getLevel().getBlockState(downPos).getBlock();
                int output = 100;
                if (belowBlock instanceof IEssenceFarmland farmland) {
                    int tier = farmland.getTier().getValue();
                    output = tier * 50 + 50;
                }

                MutableComponent outputText = Component.literal(String.valueOf(output)).append("%").withStyle(crop.getTier().getTextColor());
                MutableComponent essenceText = Component.translatable(crop.getEssenceItem().getDescriptionId());
                tooltip.add(CDPTooltips.ESSENCE_OUTPUT.args(new Object[]{essenceText,outputText}).build());
            }

            public ResourceLocation getUid() {
                return CDP_ESSENCE_CROP_BLOCK;
            }
        }, CDPEssenceCropBlock.class);
    }
}
