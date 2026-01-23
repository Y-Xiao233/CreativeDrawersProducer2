package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPTag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CDPItemTagProvider extends ItemTagsProvider {
    public CDPItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags);
    }

    protected static Item[] getDrawers(){
        List<Item> drawerList = new ArrayList<>();
        CDPBlock.CREATIVE_DRAWERS_MAP.forEach((location, register) -> {
            drawerList.add(register.asItem());
        });
        return drawerList.toArray(new Item[0]);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(CDPTag.Items.CREATIVE_DRAWERS)
                .add(getDrawers());

        tag(CDPTag.Items.CREATIVE_SHARDS)
                .add(CDPItem.SHARDS.values().stream().map(CDPItemDeferredRegister::asItem).toArray(Item[]::new));
    }
}
