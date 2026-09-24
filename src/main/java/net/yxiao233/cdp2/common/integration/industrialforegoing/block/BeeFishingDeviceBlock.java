package net.yxiao233.cdp2.common.integration.industrialforegoing.block;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.module.ModuleCore;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.BeeFishingDeviceBlockEntity;
import org.jetbrains.annotations.NotNull;

public class BeeFishingDeviceBlock extends IndustrialBlock<BeeFishingDeviceBlockEntity> {
    public BeeFishingDeviceBlock() {
        super("bee_fishing_device", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK), BeeFishingDeviceBlockEntity.class, ModuleCore.TAB_CORE);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return BeeFishingDeviceBlockEntity::new;
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}
