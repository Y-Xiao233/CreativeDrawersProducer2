package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.yxiao233.cdp2.common.integration.cataclysm.CDPCataclysmItems;

/**
 * KubeJS server event which replaces the items Cataclysm needs to activate its bosses and to open the door of seal.
 *
 * <pre>{@code
 * CDPModifyEvents.cataclysmItem(event => {
 *     event.setItem('cataclysm:ignis', Item.of('minecraft:blaze_rod'))
 *     event.setItem('cataclysm:ancient_remnant', 'minecraft:emerald')
 *     event.setItem('cataclysm:the_harbinger', 'minecraft:netherite_ingot')
 *     event.setItem('cataclysm:netherite_monstrosity', 'minecraft:nether_star')
 *     event.setItem('cataclysm:door_of_seal', 'minecraft:echo_shard')
 * })
 * }</pre>
 */
public class CataclysmItemModifyEvent implements KubeEvent {
    /** Sets the required item, an empty stack removes the entry (the vanilla item is used again) */
    public void setItem(String key, ItemStack stack){
        CDPCataclysmItems.set(key,stack == null || stack.isEmpty() ? null : stack.getItem());
    }

    /** Sets the required item by its registry name, an unknown name removes the entry */
    public void setItem(String key, String item){
        ResourceLocation id = ResourceLocation.tryParse(item);
        Item resolved = id == null ? null : BuiltInRegistries.ITEM.get(id);
        CDPCataclysmItems.set(key,resolved == null || resolved == Items.AIR ? null : resolved);
    }

    /** Drops every configuration, so all vanilla Cataclysm items are used again */
    public void clear(){
        CDPCataclysmItems.reset();
    }

    public ItemStack getItem(String key){
        Item item = CDPCataclysmItems.get(key);
        return item == null ? ItemStack.EMPTY : item.getDefaultInstance();
    }

    public String getDoorOfSealKey(){
        return CDPCataclysmItems.DOOR_OF_SEAL;
    }

    public String getTheHarbingerKey(){
        return CDPCataclysmItems.THE_HARBINGER;
    }

    public String getNetheriteMonstrosityKey(){
        return CDPCataclysmItems.NETHERITE_MONSTROSITY;
    }

    public String getAncientRemnantKey(){
        return CDPCataclysmItems.ANCIENT_REMNANT;
    }

    public String getIgnisKey(){
        return CDPCataclysmItems.IGNIS;
    }
}
