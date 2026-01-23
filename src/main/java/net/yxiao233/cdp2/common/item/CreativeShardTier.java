package net.yxiao233.cdp2.common.item;

import net.minecraft.resources.ResourceLocation;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.*;

public enum CreativeShardTier {
    CREATIVE_SHARD_1(1),
    CREATIVE_SHARD_2(2),
    CREATIVE_SHARD_3(3),
    CREATIVE_SHARD_4(4);
    private final int tier;
    CreativeShardTier(int tier){
        this.tier = tier;
    }

    public int getTier() {
        return tier;
    }
    public String getName(){
        return "creative_shard_" + tier;
    }

    public ResourceLocation getId(){
        return CreativeDrawersProducer2.makeId(getName());
    }
}
