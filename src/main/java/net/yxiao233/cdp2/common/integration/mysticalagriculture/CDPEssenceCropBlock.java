package net.yxiao233.cdp2.common.integration.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.farmland.IEssenceFarmland;
import com.blakebr0.mysticalagriculture.block.MysticalCropBlock;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class CDPEssenceCropBlock extends MysticalCropBlock {
    public CDPEssenceCropBlock(Crop crop) {
        super(crop);
        properties.noLootTable();
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        int age = state.getValue(AGE);
        int crop = 0;
        int seed = 1;
        if (age == this.getMaxAge()) {
            crop = 1;
            Vec3 vec = builder.getOptionalParameter(LootContextParams.ORIGIN);
            if (vec != null) {
                ServerLevel level = builder.getLevel();
                BlockPos pos = BlockPos.containing(vec);
                Block below = level.getBlockState(pos.below()).getBlock();
                if (below instanceof IEssenceFarmland farmland) {
                    int tier = farmland.getTier().getValue();
                    crop = (int)(0.5 * (double)tier + 0.5);
                    if (tier > 1 && tier % 2 == 0 && Math.random() < 0.5) {
                        ++crop;
                    }
                }

                double chance = this.getCrop().getSecondaryChance(below);
                if (ModConfigs.SECONDARY_SEED_DROPS.get() && Math.random() < chance) {
                    seed = 2;
                }
            }
        }

        List<ItemStack> drops = new ArrayList<>();
        if (crop > 0) {
            drops.add(new ItemStack(this.getCropsItem(), crop));
        }

        drops.add(new ItemStack(this.getBaseSeedId(), seed));
        return drops;
    }
}
