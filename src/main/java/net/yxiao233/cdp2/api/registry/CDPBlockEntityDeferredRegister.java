package net.yxiao233.cdp2.api.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.common.registry.CDPBlock;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CDPBlockEntityDeferredRegister<T extends BlockEntity> {
    private static final ArrayList<CDPBlockEntityDeferredRegister<?>> LIST = new ArrayList<>();
    private final CDPBlockDeferredRegister blockDeferredRegister;
    private final DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> entityType;
    private final boolean hasRenderer;
    private CDPBlockEntityDeferredRegister(CDPBlockDeferredRegister block, DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> entityType){
        this(block,entityType,false);
    }

    private CDPBlockEntityDeferredRegister(CDPBlockDeferredRegister block, DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> entityType, boolean hasRenderer){
        this.blockDeferredRegister = block;
        this.entityType = entityType;
        this.hasRenderer = false;
        LIST.add(this);
    }

    public static <T extends BlockEntity> CDPBlockEntityDeferredRegister<T> registrySimple(String name, Supplier<? extends Block> blockSupplier, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Item.Properties itemProperties){
        CDPBlockDeferredRegister blockWithItem = CDPBlockDeferredRegister.registrySimple(name, blockSupplier, itemProperties);
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> blockEntityType = CDPBlock.BLOCK_ENTITIES.register(name,() -> BlockEntityType.Builder.of(blockEntitySupplier,blockWithItem.asBlock()).build(null));
        return new CDPBlockEntityDeferredRegister<>(blockWithItem,blockEntityType);
    }

    public static <T extends BlockEntity> CDPBlockEntityDeferredRegister<T> register(String name, Supplier<? extends Block> blockSupplier, CDPBlockDeferredRegister.BlockItemSupplier blockItemSupplier, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier){
        CDPBlockDeferredRegister blockWithItem = CDPBlockDeferredRegister.register(name,blockSupplier,blockItemSupplier);
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> blockEntityType = CDPBlock.BLOCK_ENTITIES.register(name,() -> BlockEntityType.Builder.of(blockEntitySupplier,blockWithItem.asBlock()).build(null));
        return new CDPBlockEntityDeferredRegister<>(blockWithItem,blockEntityType);
    }

    public static CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> registerCreativeDrawer(String name, Supplier<ItemStack> infinityItem, boolean hasRenderer){
        CDPBlockDeferredRegister blockWithItem = CDPBlockDeferredRegister.registerCreativeDrawer(name,infinityItem);
        DeferredHolder<BlockEntityType<?>,BlockEntityType<CreativeDrawerBlockEntity>> blockEntityType = CDPBlock.BLOCK_ENTITIES.register(name,() -> BlockEntityType.Builder.of((pos,state) -> new CreativeDrawerBlockEntity(pos,state,infinityItem, CreativeDrawersProducer2.makeId(name)),blockWithItem.asBlock()).build(null));
        return new CDPBlockEntityDeferredRegister<>(blockWithItem,blockEntityType);
    }

    public static CDPBlockEntityDeferredRegister<CreativeDrawerBlockEntity> registerCreativeDrawer(String name, Supplier<ItemStack> infinityItem){
        return registerCreativeDrawer(name,infinityItem,false);
    }

    public static <T extends BlockEntity> CDPBlockEntityDeferredRegister<T> registrySimpleWithRenderer(String name, Supplier<? extends Block> blockSupplier, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Item.Properties itemProperties, boolean hasRenderer){
        CDPBlockDeferredRegister blockWithItem = CDPBlockDeferredRegister.registrySimple(name, blockSupplier, itemProperties);
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> blockEntityType = CDPBlock.BLOCK_ENTITIES.register(name,() -> BlockEntityType.Builder.of(blockEntitySupplier,blockWithItem.asBlock()).build(null));
        return new CDPBlockEntityDeferredRegister<>(blockWithItem,blockEntityType,hasRenderer);
    }

    public CDPBlockEntityDeferredRegister<T> addToTab(@Nonnull CDPCreativeModeTabDeferredRegister tab){
        this.blockDeferredRegister.getItemRegister().addToTab(tab);
        return this;
    }
    public DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> getBlockEntityType() {
        return entityType;
    }

    public BlockEntityType<T> asBlockEntityType() {
        return entityType.get();
    }

    public CDPBlockDeferredRegister getBlockDeferredHolder() {
        return blockDeferredRegister;
    }

    public DeferredHolder<Block,Block> getBlock(){
        return this.blockDeferredRegister.getBlock();
    }

    public Block asBlock(){
        return this.blockDeferredRegister.asBlock();
    }
    public DeferredHolder<Item,Item> getItem(){
        return this.blockDeferredRegister.getItem();
    }
    public Item asItem(){
        return this.blockDeferredRegister.asItem();
    }

    public static ArrayList<CDPBlockEntityDeferredRegister<?>> values() {
        return LIST;
    }
}
