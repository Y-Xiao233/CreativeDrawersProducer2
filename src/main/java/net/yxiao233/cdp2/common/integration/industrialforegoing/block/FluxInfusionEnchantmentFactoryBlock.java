package net.yxiao233.cdp2.common.integration.industrialforegoing.block;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.module.ModuleCore;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.FluxInfusionEnchantmentFactoryEntity;
import org.jetbrains.annotations.NotNull;

public class FluxInfusionEnchantmentFactoryBlock extends IndustrialBlock<FluxInfusionEnchantmentFactoryEntity> {
    public FluxInfusionEnchantmentFactoryBlock() {
        super("flux_enchantment_factory", Properties.ofFullCopy(Blocks.IRON_BLOCK), FluxInfusionEnchantmentFactoryEntity.class, ModuleCore.TAB_CORE);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return FluxInfusionEnchantmentFactoryEntity::new;
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}
