package net.yxiao233.cdp2.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CDPBlockLootTables extends BlockLootSubProvider {
    public CDPBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        CDPBlock.CREATIVE_DRAWERS_MAP.forEach((location, register) -> {
            this.dropSelf(register.asBlock());
        });
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        //Adding ".nonLootTable()" after the registration method will not generate it here
        ArrayList<Block> list = new ArrayList<>();
        CDPBlock.BLOCKS.getEntries().forEach(entry ->{
            list.add(entry.get());
        });
        return List.copyOf(list);
    }
}

