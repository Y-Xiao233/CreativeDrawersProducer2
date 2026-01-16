package net.yxiao233.cdp2.api.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.yxiao233.cdp2.api.annotation.AutoRegistryBlockCapabilities;
import net.yxiao233.cdp2.api.block.entity.CDPCapabilitiesBlockEntity;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.util.AnnotationUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class BlockCapabilityMap {
    @AutoRegistryBlockCapabilities
    public static final BlockCapability<IItemHandler, Direction> ITEM_HANDLER = Capabilities.ItemHandler.BLOCK;
    @AutoRegistryBlockCapabilities
    public static final BlockCapability<IFluidHandler, Direction> FLUID_HANDLER = Capabilities.FluidHandler.BLOCK;
    @AutoRegistryBlockCapabilities
    public static final BlockCapability<IEnergyStorage, Direction> ENERGY_STORAGE = Capabilities.EnergyStorage.BLOCK;
    private final HashMap<BlockCapability<?, ?>,Object> map = new HashMap<>();
    public static final List<BlockCapability<?,?>> list = new ArrayList<>();
    public static void init(){
        AnnotationUtil.getAllFields(AutoRegistryBlockCapabilities.class).forEach(field -> {
            try {
                if(Modifier.isStatic(field.getModifiers())){
                    BlockCapabilityMap.registryCapability((BlockCapability<?, ?>) field.get(null));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private BlockCapabilityMap(){
    }

    public static BlockCapabilityMap create(){
        return new BlockCapabilityMap();
    }

    public <T, C> BlockCapabilityMap add(BlockCapability<T, C> capability, T provider){
        if(map.containsKey(capability)){
            return this;
        }
        map.put(capability,provider);
        return this;
    }

    public <T extends IItemHandler & INBTSerializable<CompoundTag>> BlockCapabilityMap addItemHandler(T provider){
        return add(ITEM_HANDLER,provider);
    }

    public <T extends IFluidHandler & INBTSerializable<CompoundTag>> BlockCapabilityMap addFluidHandler(T provider){
        return add(FLUID_HANDLER,provider);
    }

    public <T extends IEnergyStorage & INBTSerializable<CompoundTag>> BlockCapabilityMap addEnergyStorage(T provider){
        return add(ENERGY_STORAGE,provider);
    }

    @SuppressWarnings("unchecked")
    public <T, C, P extends T> P get(BlockCapability<T, C> capability){
        return (P) map.get(capability);
    }

    public <T extends IItemHandler> T getItemHandler(){
        return get(ITEM_HANDLER);
    }

    public <T extends IItemHandler> T getItemHandler(Class<T> clazz){
        return get(ITEM_HANDLER);
    }

    @SuppressWarnings("unchecked")
    public void saveAll(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries){
        map.forEach((blockCapability, provider) -> {
            if(provider instanceof INBTSerializable<?> s){
                INBTSerializable<CompoundTag> serializable = (INBTSerializable<CompoundTag>) s;
                tag.put(blockCapability.name().getPath(),serializable.serializeNBT(registries));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public void loadAll(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries){
        map.forEach((blockCapability, provider) -> {
            if(provider instanceof INBTSerializable<?> s){
                INBTSerializable<CompoundTag> serializable = (INBTSerializable<CompoundTag>) s;
                serializable.deserializeNBT(registries,tag.getCompound(blockCapability.name().getPath()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <E extends CDPCapabilitiesBlockEntity, T, C> void registryAll(RegisterCapabilitiesEvent event){
        init();
        CDPBlockEntityDeferredRegister.values().forEach(register ->{
            if(AnnotationUtil.isAnnotationPresent(AutoRegistryBlockCapabilities.class,register.asBlock())){
                list.forEach(capability ->{
                    BlockCapability<T,C> blockCapability = (BlockCapability<T, C>) capability;
                    BlockEntityType<E> blockEntityType = (BlockEntityType<E>) register.asBlockEntityType();
                    event.registerBlockEntity(blockCapability,blockEntityType,(entity, direction) ->{
                        return (T) entity.getCapabilityMap().map.get(blockCapability);
                    });
                } );
            }
        });
    }

    public static void registryCapability(BlockCapability<?,?> capability){
        list.add(capability);
    }
}
