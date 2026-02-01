package net.yxiao233.cdp2.common.event;

import com.buuz135.industrial.block.IndustrialBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.block.tile.PoweredTile;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.capabilities.BlockCapabilityMap;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.common.registry.CDPBlock;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RegisterCapabilitiesHandler {

    @SubscribeEvent
    public static void onRegister(RegisterCapabilitiesEvent event){
        BlockCapabilityMap.registryAll(event);

        register(event,CDPBlock.VOID_SIEVE.type());

        CDPBlock.POTS_MAP.values().forEach(pot ->{
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, pot.asBlockEntityType(), (blockEntity, side) -> {
                return side == Direction.DOWN ? new SidedInvWrapper(blockEntity, Direction.DOWN) : null;
            });
        });
    }

    public static void register(RegisterCapabilitiesEvent event, Holder<BlockEntityType<?>> type){
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, (BlockEntityType<?>)type.value(), (object, context) -> {
            if (object instanceof PoweredTile<?> powered) {
                return powered.getEnergyStorage();
            } else {
                return null;
            }
        });
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType<?>)type.value(), (object, context) -> {
            if (object instanceof ActiveTile<?> tile) {
                return tile.getFluidHandler(context);
            } else {
                return null;
            }
        });
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType<?>)type.value(), (object, context) -> {
            if (object instanceof ActiveTile<?> tile) {
                return tile.getItemHandler(context);
            } else {
                return null;
            }
        });
    }
}
