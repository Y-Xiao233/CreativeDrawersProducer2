package net.yxiao233.cdp2.common.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public class CDPTag {
    public static class Items{
        public static final TagKey<Item> CREATIVE_DRAWERS = createTag("creative_drawers");
        public static final TagKey<Item> CREATIVE_SHARDS = createTag("creative_shards");
        public static final TagKey<Item> BOTANY_POTS = createTag("botany_pots");
        private static TagKey<Item> createTag(String name){
            return ItemTags.create(CreativeDrawersProducer2.makeId(name));
        }
    }

    public static class Blocks{
        public static final TagKey<Block> CREATIVE_DRAWERS = createTag("creative_drawers");
        public static final TagKey<Block> BOTANY_POTS = createTag("botany_pots");
        private static TagKey<Block> createTag(String name){
            return BlockTags.create(CreativeDrawersProducer2.makeId(name));
        }
    }
}
