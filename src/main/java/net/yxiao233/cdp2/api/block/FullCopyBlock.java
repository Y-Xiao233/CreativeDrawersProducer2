package net.yxiao233.cdp2.api.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FullCopyBlock extends Block {
    public FullCopyBlock(BlockBehaviour blockBehaviour) {
        super(Properties.ofFullCopy(blockBehaviour));
    }
}
