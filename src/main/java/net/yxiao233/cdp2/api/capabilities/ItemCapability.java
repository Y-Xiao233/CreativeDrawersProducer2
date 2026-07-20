package net.yxiao233.cdp2.api.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemCapability<T extends ItemStackHandler> implements BlockHandler<IItemHandler> {
    private final T handler;
    private String serializeKey = "Items";
    public ItemCapability(T handler, String serializeKey){
        this.handler = handler;
        this.serializeKey = serializeKey;
    }
    public ItemCapability(T handler){
        this.handler = handler;
    }
    @Override
    public T getHandler() {
        return handler;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put(serializeKey,handler.serializeNBT(registries));
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        handler.deserializeNBT(registries,tag.getCompound(serializeKey));
    }

    @Override
    public BlockCapability<IItemHandler, Direction> getCapability() {
        return Capabilities.ItemHandler.BLOCK;
    }

}
