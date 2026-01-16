package net.yxiao233.cdp2.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.api.block.ILeftClickHandler;
import net.yxiao233.cdp2.api.block.IRightClickedHandler;
import net.yxiao233.cdp2.api.block.entity.CDPCapabilitiesBlockEntity;
import net.yxiao233.cdp2.api.block.entity.ITickableBlockEntity;
import net.yxiao233.cdp2.api.capabilities.BigItemStackHandler;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.util.EntityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CreativeDrawerBlockEntity extends CDPCapabilitiesBlockEntity implements ITickableBlockEntity, ILeftClickHandler, IRightClickedHandler {
    private final BlockCapabilityMap capabilityMap = BlockCapabilityMap.create()
            .addItemHandler(new BigItemStackHandler(1,Integer.MAX_VALUE){
                @Override
                protected void onContentsChanged(int slot) {
                    setChanged();
                    if(getLevel() != null && !getLevel().isClientSide()){
                        getLevel().sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
                    }
                }

                @Override
                public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                    return ItemStack.isSameItemSameComponents(stack,infinityItem);
                }

                @Override
                public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                    return isItemValid(slot,stack) ? ItemStack.EMPTY : stack;
                }
            });
    private final ItemStack infinityItem;

    private int removeTicks = 0;
    public CreativeDrawerBlockEntity(BlockPos pos, BlockState blockState, Supplier<ItemStack> infinityItem, ResourceLocation entityTypeLocation) {
        super(CDPBlock.CREATIVE_DRAWERS_MAP.get(entityTypeLocation).asBlockEntityType(), pos, blockState);
        this.infinityItem = infinityItem.get().copyWithCount(Integer.MAX_VALUE);
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        this.removeTicks = Math.max(this.removeTicks - 1, 0);
        BigItemStackHandler itemHandler = capabilityMap.getItemHandler(BigItemStackHandler.class);
        if(itemHandler != null && itemHandler.getStackInSlot(0).getCount() < Integer.MAX_VALUE){
            itemHandler.setStackInSlot(0, infinityItem);
        }
    }

    @Override
    public BlockCapabilityMap getCapabilityMap() {
        return capabilityMap;
    }

    @Override
    public void onLeftClicked(LivingEntity entity, BlockEntity blockEntity) {
        if (getLevel() != null && !getLevel().isClientSide() && entity instanceof Player player) {
            if(this.removeTicks > 0){
                return;
            }
            this.removeTicks = 3;
            BigItemStackHandler handler = getCapabilityMap().getItemHandler(BigItemStackHandler.class);
            ItemHandlerHelper.giveItemToPlayer(player,handler.extractItem(0, player.isShiftKeyDown() ? handler.getStackInSlot(0).getMaxStackSize() : 1, false));
        }
    }

    @Override
    public void onRightClicked(LivingEntity entity, BlockEntity blockEntity) {
        if (getLevel() != null && !getLevel().isClientSide() && entity instanceof Player player) {
            ItemStack mainHandItem = player.getMainHandItem();
            if(ItemStack.isSameItemSameComponents(mainHandItem,infinityItem)){
                player.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            }else{
                Inventory inventory = player.getInventory();
                for (ItemStack itemStack : inventory.items) {
                    if (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack,infinityItem)) {
                        itemStack.setCount(0);
                    }
                }
            }
        }
    }

    public ItemStack getInfinityItem() {
        return infinityItem;
    }

    public static Direction getHitDirection(Level level, LivingEntity entity){
        HitResult rayTraceResult = EntityUtil.rayTraceSimple(level, entity, 16, 0);
        if (rayTraceResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockResult = (BlockHitResult) rayTraceResult;
            return blockResult.getDirection();
        }
        return null;
    }
}
