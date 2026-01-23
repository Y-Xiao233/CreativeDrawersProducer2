package net.yxiao233.cdp2.api.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.item.CreativeDrawerBlockItem;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.common.registry.CDPBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CDPBlockDeferredRegister {
    private final CDPItemDeferredRegister itemRegister;
    private final DeferredHolder<Item,Item> itemDeferredHolder;
    private final DeferredHolder<Block,Block> blockBlockDeferredHolder;
    private CDPBlockDeferredRegister(DeferredHolder<Block,Block> blockBlockDeferredHolder, CDPItemDeferredRegister itemRegister){
        this.itemRegister = itemRegister;
        this.itemDeferredHolder = itemRegister.getItemHolder();
        this.blockBlockDeferredHolder = blockBlockDeferredHolder;
    }
    public static CDPBlockDeferredRegister registrySimple(String name, Supplier<? extends Block> sup, @Nullable Item.Properties itemProperties){
        DeferredHolder<Block,Block> block = registryBlock(name,sup);
        CDPItemDeferredRegister item = registryItem(name,block,itemProperties);
        return new CDPBlockDeferredRegister(block,item);
    }

    public static CDPBlockDeferredRegister register(String name, Supplier<? extends Block> blockSupplier, BlockItemSupplier blockItemSupplier){
        DeferredHolder<Block, Block> block = registryBlock(name,blockSupplier);
        CDPItemDeferredRegister item = CDPItemDeferredRegister.registryItem(name,blockItemSupplier.accept(block));
        return new CDPBlockDeferredRegister(block,item);
    }

    public static CDPBlockDeferredRegister registerCreativeDrawer(String name, Supplier<ItemStack> infinityItem){
        DeferredHolder<Block,Block> block = registryBlock(name,() -> new CreativeDrawerBlock(infinityItem, CreativeDrawersProducer2.makeId(name)));
        CDPItemDeferredRegister item = CDPItemDeferredRegister.registryItem(name,() -> new CreativeDrawerBlockItem(block.get(), infinityItem, new Item.Properties()));
        return new CDPBlockDeferredRegister(block,item);
    }

    public CDPBlockDeferredRegister addToTab(@Nonnull CDPCreativeModeTabDeferredRegister tab){
        this.itemRegister.addToTab(tab);
        return this;
    }

    private static DeferredHolder<Block,Block> registryBlock(String name, Supplier<? extends Block> sup){
        return CDPBlock.BLOCKS.register(name,sup);
    }

    private static CDPItemDeferredRegister registryItem(String name, DeferredHolder<Block,Block> block, @Nullable Item.Properties properties){
        return CDPItemDeferredRegister.registryItem(name,() -> new BlockItem(block.get(), properties == null ? new Item.Properties() : properties));
    }

    public DeferredHolder<Item,Item> getItem(){
        return this.itemDeferredHolder;
    }

    public DeferredHolder<Block, Block> getBlock() {
        return this.blockBlockDeferredHolder;
    }

    public Item asItem(){
        return this.itemDeferredHolder.get();
    }

    public Block asBlock(){
        return this.blockBlockDeferredHolder.get();
    }

    public ItemStack asItemStack(){
        return this.itemDeferredHolder.get().getDefaultInstance();
    }

    public BlockState asBlockState(){
        return this.blockBlockDeferredHolder.get().defaultBlockState();
    }

    public CDPItemDeferredRegister getItemRegister() {
        return itemRegister;
    }

    @FunctionalInterface
    public interface BlockItemSupplier{
        Supplier<? extends BlockItem> accept(Supplier<Block> blockSupplier);
    }
}
