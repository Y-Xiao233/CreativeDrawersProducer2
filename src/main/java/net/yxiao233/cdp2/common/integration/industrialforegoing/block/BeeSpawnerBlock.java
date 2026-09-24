package net.yxiao233.cdp2.common.integration.industrialforegoing.block;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.module.ModuleCore;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.BeeSpawnerBlockEntity;
import org.jetbrains.annotations.NotNull;

public class BeeSpawnerBlock extends IndustrialBlock<BeeSpawnerBlockEntity> {
    public BeeSpawnerBlock() {
        super("bee_spawner", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK), BeeSpawnerBlockEntity.class, ModuleCore.TAB_CORE);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return BeeSpawnerBlockEntity::new;
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}
