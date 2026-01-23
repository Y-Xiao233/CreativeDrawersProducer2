package net.yxiao233.cdp2.common.item;

import net.minecraft.world.item.Item;

public class CreativeShardItem extends Item {
    private final int tier;
    public CreativeShardItem(Properties properties, int tier) {
        super(properties);
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
}
