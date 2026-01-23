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
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CDPBlockTagProvider extends BlockTagsProvider {
    public CDPBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CreativeDrawersProducer2.MODID, existingFileHelper);
    }

    protected static Block[] getDrawers(){
        List<Block> drawerList = new ArrayList<>();
        CDPBlock.CREATIVE_DRAWERS_MAP.forEach((location, register) -> {
            drawerList.add(register.asBlock());
        });
        return drawerList.toArray(new Block[0]);
    }
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        Block[] drawers = getDrawers();

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(drawers);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(CDPBlock.UPGRADE_STATION.asBlock());

        this.tag(CDPTag.Blocks.CREATIVE_DRAWERS)
                .add(drawers);
    }
}
