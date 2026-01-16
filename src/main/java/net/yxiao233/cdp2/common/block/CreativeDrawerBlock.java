package net.yxiao233.cdp2.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.annotation.AutoRegistryBlockCapabilities;
import net.yxiao233.cdp2.api.block.CDPTickableEntityBlock;
import net.yxiao233.cdp2.api.block.entity.ITickableBlockEntity;
import net.yxiao233.cdp2.api.block.property.IRotatableBlock;
import net.yxiao233.cdp2.api.block.property.RotationType;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@AutoRegistryBlockCapabilities
public class CreativeDrawerBlock extends CDPTickableEntityBlock implements IRotatableBlock{
    private final Supplier<ItemStack> infinityItem;
    private final ResourceLocation entityTypeLocation;
    public CreativeDrawerBlock(Properties properties, Supplier<ItemStack> infinityItem, ResourceLocation entityTypeLocation) {
        super(properties.sound(SoundType.WOOD).strength(0.3F));
        this.infinityItem = infinityItem;
        this.entityTypeLocation = entityTypeLocation;
    }

    public CreativeDrawerBlock(Supplier<ItemStack> infinityItem, ResourceLocation entityTypeLocation) {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.WOOD).strength(0.3F));
        this.infinityItem = infinityItem;
        this.entityTypeLocation = entityTypeLocation;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(properties -> new CreativeDrawerBlock(properties,infinityItem,entityTypeLocation));
    }

    @Override
    @SuppressWarnings("unchecked")
    public BlockEntityType<? extends ITickableBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ITickableBlockEntity>) CDPBlock.CREATIVE_DRAWERS_MAP.get(entityTypeLocation).asBlockEntityType();
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CreativeDrawerBlockEntity(blockPos,blockState,infinityItem,entityTypeLocation);
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    public Supplier<ItemStack> getInfinityItem() {
        return infinityItem;
    }
}
