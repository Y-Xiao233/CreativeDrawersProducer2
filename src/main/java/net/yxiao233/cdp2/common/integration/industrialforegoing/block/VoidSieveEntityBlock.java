package net.yxiao233.cdp2.common.integration.industrialforegoing.block;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.module.ModuleCore;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity.VoidSieveBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoidSieveEntityBlock extends IndustrialBlock<VoidSieveBlockEntity> {
    public VoidSieveEntityBlock() {
        super("sieve", Properties.ofFullCopy(Blocks.IRON_BLOCK), VoidSieveBlockEntity.class, ModuleCore.TAB_CORE);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return VoidSieveBlockEntity::new;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.cdp2.void_power"));
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}
