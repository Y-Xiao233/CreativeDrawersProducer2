package net.yxiao233.cdp2.integration.botany_pot;

import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.darkhax.botanypots.common.addons.jei.BotanyPotsJEIPlugin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BotanyPotJei {
    public static final List<Block> POTS = List.of(
            get("botanypots:terracotta_botany_pot"),
            get("botanypots:terracotta_hopper_botany_pot"),
            get("botanypotstiers:elite_terracotta_botany_pot"),
            get("botanypotstiers:elite_terracotta_hopper_botany_pot"),
            get("botanypotstiers:ultra_terracotta_botany_pot"),
            get("botanypotstiers:ultra_terracotta_hopper_botany_pot"),
            get("botanypotstiers:mega_terracotta_botany_pot"),
            get("botanypotstiers:mega_terracotta_hopper_botany_pot")
    );
    public static void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration){
        POTS.forEach(pot -> registration.addRecipeCatalyst(pot,BotanyPotsJEIPlugin.CROP));
        CDPBlock.POTS_MAP.values().forEach(pot -> registration.addRecipeCatalyst(pot.asItem(),BotanyPotsJEIPlugin.CROP));
    }

    public static Block get(String id){
        return BuiltInRegistries.BLOCK.get(ResourceLocation.parse(id));
    }
}
