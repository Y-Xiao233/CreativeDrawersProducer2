package net.yxiao233.cdp2.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;

import java.util.HashMap;
import java.util.function.Supplier;

public class CDPBlock {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, CreativeDrawersProducer2.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CreativeDrawersProducer2.MODID);
    public static final HashMap<ResourceLocation,CDPBlockEntityDeferredRegister<?>> CREATIVE_DRAWERS_MAP = new HashMap<>();
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> DIAMOND_CREATIVE_DRAWER = registerCreativeDrawer("diamond_creative_drawer", Items.DIAMOND::getDefaultInstance);
    public static final CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> OAK_LOG_CREATIVE_DRAWER = registerCreativeDrawer("oak_log_creative_drawer", Items.OAK_LOG::getDefaultInstance);

    static <T extends BlockEntity> CDPBlockEntityDeferredRegister<T> registrySimple(String name, BlockSupplier<?> blockSupplier, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier){
        return CDPBlockEntityDeferredRegister.registrySimple(name,() -> blockSupplier.create(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)),blockEntitySupplier,new Item.Properties());
    }

    static CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> registerCreativeDrawer(String name, Supplier<ItemStack> infinityItem){
        CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> register = CDPBlockEntityDeferredRegister.registerCreativeDrawer(name, infinityItem).addToTab(CDPTab.DRAWER_TAB);
        CREATIVE_DRAWERS_MAP.put(CreativeDrawersProducer2.makeId(name),register);
        return register;
    }

    @FunctionalInterface
    interface BlockSupplier<T extends Block> {
        T create(BlockBehaviour.Properties properties);
    }
}
