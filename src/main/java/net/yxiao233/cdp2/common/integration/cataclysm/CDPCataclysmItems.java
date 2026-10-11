package net.yxiao233.cdp2.common.integration.cataclysm;

import com.github.L_Ender.cataclysm.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The items Cataclysm requires to activate its bosses and to open the door of seal.
 *
 * <p>Nothing is configured by default, in that case the vanilla items are used (the callers fall back to them). KubeJS
 * scripts replace them through {@code CDPModifyEvents.cataclysmItem}, see
 * {@link net.yxiao233.cdp2.common.integration.kubejs.event.CataclysmItemModifyEvent}.</p>
 */
public class CDPCataclysmItems {
    /** Required to open the door of seal, vanilla: {@code cataclysm:strange_key} */
    public static final String DOOR_OF_SEAL = "cataclysm:door_of_seal";
    /** Required to activate the harbinger, vanilla: {@code minecraft:nether_star} (and {@code cataclysm:mech_eye} for its boss respawner) */
    public static final String THE_HARBINGER = "cataclysm:the_harbinger";
    /** Required to activate the netherite monstrosity / its boss respawner, defaults to {@code minecraft:nether_star} */
    public static final String NETHERITE_MONSTROSITY = "cataclysm:netherite_monstrosity";
    /** Required to activate the ancient remnant (awakening it and its boss respawner), vanilla: {@code cataclysm:necklace_of_the_desert} */
    public static final String ANCIENT_REMNANT = "cataclysm:ancient_remnant";
    /** Required to activate Ignis (the altar of fire and its boss respawner), vanilla: {@code cataclysm:burning_ashes} */
    public static final String IGNIS = "cataclysm:ignis";

    private static final Map<String,Item> ITEMS = new ConcurrentHashMap<>();

    public static void reset(){
        ITEMS.clear();
    }

    public static void set(String key, @Nullable Item item){
        if(item == null){
            ITEMS.remove(key);
        }else{
            ITEMS.put(key,item);
        }
    }

    @Nullable
    public static Item get(@Nullable String key){
        return key == null ? null : ITEMS.get(key);
    }

    /** The configured item, or {@code fallback} when nothing is configured for that key */
    public static Item get(@Nullable String key, Item fallback){
        Item item = get(key);
        return item == null ? fallback : item;
    }

    /** The configuration key of the boss a boss respawner spawns, or null for an unknown boss */
    @Nullable
    public static String keyOf(EntityType<?> type){
        if(type == ModEntities.THE_HARBINGER.get()){
            return THE_HARBINGER;
        }
        if(type == ModEntities.NETHERITE_MONSTROSITY.get()){
            return NETHERITE_MONSTROSITY;
        }
        if(type == ModEntities.ANCIENT_REMNANT.get()){
            return ANCIENT_REMNANT;
        }
        if(type == ModEntities.IGNIS.get()){
            return IGNIS;
        }
        return null;
    }
}
