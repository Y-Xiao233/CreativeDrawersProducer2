package net.yxiao233.cdp2.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LevelUtil {
    public static void dropContents(Level level, BlockPos pos, ItemStack stack){
        double d0 = (double) EntityType.ITEM.getHeight() / 2.0;
        double d1 = (double)pos.getX() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
        double d2 = (double)pos.getY() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25) - d0;
        double d3 = (double)pos.getZ() + 0.5 + Mth.nextDouble(level.random, -0.25, 0.25);
        ItemEntity item = new ItemEntity(level, d1, d2, d3, stack);
        level.addFreshEntity(item);
    }

    public static boolean checkDimension(ResourceKey<Level> dimension1, ResourceKey<Level> dimension2) {
        if (dimension1 == null || dimension2 == null) {
            return false;
        }
        return dimension1.equals(dimension2);
    }
}
