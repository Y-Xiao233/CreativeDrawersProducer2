package net.yxiao233.cdp2.common.integration.kubejs.item;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.yxiao233.ifs.api.item.FortuneAddonItem;

public class FortuneAddonItemBuilder extends ItemBuilder {
    private int tier = 1;
    public FortuneAddonItemBuilder(ResourceLocation id) {
        super(id);
    }

    public FortuneAddonItemBuilder setTier(int tier){
        this.tier = tier;
        return this;
    }

    @Override
    public Item createObject() {
        return new FortuneAddonItem(tier);
    }
}
