package net.yxiao233.cdp2.api.block;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ILeftClickHandler {
    void onLeftClicked(LivingEntity entity, BlockEntity blockEntity);
}
