package net.yxiao233.cdp2.api.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;

public class DefinedBlockCapability {
    private final BlockCapability<?,?> capability;
    private final Object provider;
    private BiFunction<? extends BlockEntity,?,?> rule;
    public <T,C> DefinedBlockCapability(BlockCapability<T,C> capability, T provider, @Nullable BiFunction<? extends BlockEntity,C,T> rule){
        this.capability = capability;
        this.provider = provider;
        this.rule = Objects.requireNonNullElseGet(rule, () -> (entity, o) -> provider);
    }

    @SuppressWarnings("unchecked")
    public <T, C> T getCapability(BlockCapability<T,C> capability) {
        if(this.capability.equals(capability)){
            return (T) this.provider;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T, C> BiFunction<BlockEntity, C, T> getRule(BlockCapability<T,C> capability) {
        if(this.capability.equals(capability)){
            return (BiFunction<BlockEntity, C, T>) this.rule;
        }
        return null;
    }
}
