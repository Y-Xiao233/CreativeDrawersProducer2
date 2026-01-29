package net.yxiao233.cdp2.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.api.annotation.AutoRegistryBlockCapabilities;
import net.yxiao233.cdp2.api.block.CDPMachineEntityBlock;
import net.yxiao233.cdp2.api.block.entity.ITickableBlockEntity;
import net.yxiao233.cdp2.api.block.property.IRotatableBlock;
import net.yxiao233.cdp2.api.block.property.RotationType;
import net.yxiao233.cdp2.common.block.entity.UpgradeStationBlockEntity;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPDataComponentTypes;
import net.yxiao233.cdp2.util.LevelUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AutoRegistryBlockCapabilities
public class UpgradeStationBlock extends CDPMachineEntityBlock<UpgradeStationBlockEntity> implements IRotatableBlock{
    public UpgradeStationBlock(Properties properties) {
        super(properties);
    }
    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(UpgradeStationBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new UpgradeStationBlockEntity(blockPos,blockState);
    }

    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        UpgradeStationBlockEntity.entries.get(pos).removeMekanismUpgrade(level,-1);
        createDrop(level,UpgradeStationBlockEntity.entries.get(pos));
        UpgradeStationBlockEntity.entries.remove(pos);
    }

    private void createDrop(Level level, UpgradeStationBlockEntity entity){
        ItemStack stack = new ItemStack(this.asItem());
        MinecraftServer server = level.getServer();
        if(server != null){
            CompoundTag raw = stack.getOrDefault(CDPDataComponentTypes.COMPOUND_TAG, new CompoundTag());
            raw.put("entity_data",entity.serializeNBT(server.registryAccess()));
            System.out.println(raw);
            stack.set(CDPDataComponentTypes.COMPOUND_TAG,raw);
            System.out.println(stack.get(CDPDataComponentTypes.COMPOUND_TAG));
            LevelUtil.dropContents(level,entity.getBlockPos(),stack);
        }
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        MinecraftServer server = level.getServer();
        if(server != null){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if(blockEntity instanceof UpgradeStationBlockEntity upgradeStationBlockEntity){
                if(stack.has(CDPDataComponentTypes.COMPOUND_TAG)){
                    CompoundTag tag = stack.get(CDPDataComponentTypes.COMPOUND_TAG);
                    if(tag != null && tag.contains("entity_data")){
                        upgradeStationBlockEntity.deserializeNBT(server.registryAccess(),tag.getCompound("entity_data"));
                    }
                }else if(upgradeStationBlockEntity.getOwner() == null && placer != null){
                    upgradeStationBlockEntity.setOwner(placer.getUUID());
                }
            }
        }
    }


    @Override
    public BlockEntityType<? extends ITickableBlockEntity> getBlockEntityType() {
        return CDPBlock.UPGRADE_STATION.asBlockEntityType();
    }

    @Override
    public @NotNull RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }
}
