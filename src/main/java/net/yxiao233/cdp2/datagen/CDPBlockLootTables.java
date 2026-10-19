package net.yxiao233.cdp2.datagen;

import com.sammy.malum.registry.common.item.MalumItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.yxiao233.cdp2.common.integration.mysticalagriculture.CDPEssenceCropBlock;
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
        CDPBlock.CREATIVE_DRAWERS_MAP.values().forEach(drawer -> this.dropSelf(drawer.asBlock()));
        CDPBlock.POTS_MAP.values().forEach(pot -> this.dropSelf(pot.asBlock()));
        this.dropSelf(CDPBlock.ABSOLUTE_FARMLAND.asBlock());
        this.dropSelf(CDPBlock.SUPREME_FARMLAND.asBlock());
        this.dropSelf(CDPBlock.COSMIC_FARMLAND.asBlock());
        this.dropSelf(CDPBlock.INFINITE_FARMLAND.asBlock());
        this.dropSelf(CDPBlock.VOID_SIEVE.getBlock());
        this.dropSelf(CDPBlock.VOID_BLOCK.asBlock());
        this.dropSelf(CDPBlock.VOID_CRAFTING_TABLE.asBlock());
        this.dropSelf(CDPBlock.FRAME.asBlock());
        this.dropSelf(CDPBlock.FLUX_INFUSION_ENCHANTMENT_FACTORY.getBlock());
        this.dropSelf(CDPBlock.UNKNOWN_BLOCK.asBlock());
        this.dropSelf(CDPBlock.BREEDING_CHAMBER.getBlock());
        this.dropSelf(CDPBlock.BEE_CONVERTER.getBlock());
        this.dropSelf(CDPBlock.BEE_FISHING_DEVICE.getBlock());
        this.dropSelf(CDPBlock.BEE_SPAWNER.getBlock());
        //twisted ores: silk touch drops the block itself, otherwise four malum raw materials (fortune still applies, no experience)
        this.add(CDPBlock.TWISTED_CTHONIC_GOLD_ORE.asBlock(),this.twistedOreDrop(CDPBlock.TWISTED_CTHONIC_GOLD_ORE.asBlock(),MalumItems.CTHONIC_GOLD_FRAGMENT.get()));
        this.add(CDPBlock.TWISTED_SOULSTONE_ORE.asBlock(),this.twistedOreDrop(CDPBlock.TWISTED_SOULSTONE_ORE.asBlock(),MalumItems.RAW_SOULSTONE.get()));
        this.add(CDPBlock.TWISTED_BRILLIANT_STONE.asBlock(),this.twistedOreDrop(CDPBlock.TWISTED_BRILLIANT_STONE.asBlock(),MalumItems.RAW_BRILLIANCE.get()));
    }

    /**
     * Same as {@link #createOreDrop(Block, Item)} (silk touch keeps the block, explosion decay and fortune still work)
     * but the base drop is four items instead of one.
     */
    private LootTable.Builder twistedOreDrop(Block block, Item item){
        LootItem.Builder<?> drop = LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
                .apply(ApplyBonusCount.addOreBonusCount(this.registries.holderOrThrow(Enchantments.FORTUNE)));
        return this.createSilkTouchDispatchTable(block,this.applyExplosionDecay(block,drop));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        //Adding ".nonLootTable()" after the registration method will not generate it here
        ArrayList<Block> list = new ArrayList<>();
        CDPBlock.BLOCKS.getEntries().forEach(entry ->{
            if(!(entry.get() instanceof CDPEssenceCropBlock)){
                list.add(entry.get());
            }
        });
        return List.copyOf(list);
    }
}

