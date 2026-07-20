package net.yxiao233.cdp2.api.capabilities;


import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BlockCapabilityMap {
    private final List<BlockHandler<?>> handlers = new ArrayList<>();
    private BlockCapabilityMap(){

    }

    public List<BlockHandler<?>> getHandlers() {
        return handlers;
    }

    public static BlockCapabilityMap create(){
        return new BlockCapabilityMap();
    }
    public static BlockCapabilityMap create(BlockHandler<?> handler){
        BlockCapabilityMap map = new BlockCapabilityMap();
        map.handlers.add(handler);

        return map;
    }

    public BlockCapabilityMap add(BlockHandler<?> handler){
        this.handlers.add(handler);
        return this;
    }

    public void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        handlers.forEach(handler -> {
            handler.saveAdditional(tag,registries);
        });
    }

    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        handlers.forEach(handler -> {
            handler.loadAdditional(tag,registries);
        });
    }

    @SuppressWarnings("unchecked")
    public <C, H extends BlockHandler<C>> H getHandler(BlockCapability<C, Direction> capability, Class<H> handlerClazz){
        return (H) handlers.stream().filter(handler -> handler.getCapability() == capability).findFirst().get();
    }

    @SuppressWarnings("unchecked")
    public <C> BlockHandler<C> getHandler(BlockCapability<C, Direction> capability){
        return (BlockHandler<C>) handlers.stream().filter(handler -> handler.getCapability() == capability).findFirst().get();
    }
}
