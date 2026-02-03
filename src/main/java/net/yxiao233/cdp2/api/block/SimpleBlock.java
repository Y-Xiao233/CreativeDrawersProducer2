package net.yxiao233.cdp2.api.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SimpleBlock extends Block {
    public SimpleBlock() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK));
    }
}
