package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPTag;
import net.yxiao233.cdp2.util.DataGenUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CDPBlockTagProvider extends BlockTagsProvider {
    public CDPBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreativeDrawersProducer2.MODID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        Block[] drawers = DataGenUtil.blockEntityMapForBlocks(CDPBlock.CREATIVE_DRAWERS_MAP);
        Block[] pots = DataGenUtil.typedBlockEntityMapForBlocks(CDPBlock.POTS_MAP);

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(drawers);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CDPBlock.UPGRADE_STATION.asBlock())
                .add(pots);

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(CDPBlock.ABSOLUTE_FARMLAND.asBlock())
                .add(CDPBlock.SUPREME_FARMLAND.asBlock())
                .add(CDPBlock.COSMIC_FARMLAND.asBlock())
                .add(CDPBlock.INFINITE_FARMLAND.asBlock());

        this.tag(CDPTag.Blocks.CREATIVE_DRAWERS)
                .add(drawers);

        this.tag(CDPTag.Blocks.BOTANY_POTS)
                .add(pots);
    }
}
