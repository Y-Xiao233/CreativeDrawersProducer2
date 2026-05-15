package net.yxiao233.cdp2.datagen;

import io.github.lounode.ae2cs.common.init.AECSTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.common.integration.kubejs.event.RegistryDefinitionEvent;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.common.registry.CDPTag;
import net.yxiao233.cdp2.util.DataGenUtil;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class CDPItemTagProvider extends ItemTagsProvider {
    public CDPItemTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags) {
        super(pOutput, pLookupProvider, pBlockTags);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(CDPTag.Items.CREATIVE_DRAWERS)
                .add(DataGenUtil.blockEntityMapForItems(CDPBlock.CREATIVE_DRAWERS_MAP));

        tag(CDPTag.Items.CREATIVE_SHARDS)
                .add(DataGenUtil.itemMapForItems(CDPItem.SHARDS));

        tag(CDPTag.Items.BOTANY_POTS)
                .add(DataGenUtil.typedBlockEntityMapForItems(CDPBlock.POTS_MAP));
    }
}
