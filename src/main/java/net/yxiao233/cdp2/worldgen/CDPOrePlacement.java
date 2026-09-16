package net.yxiao233.cdp2.worldgen;

import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class CDPOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange){
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange){
        return orePlacement(CountPlacement.of(count),heightRange);
    }

    public static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange){
        return orePlacement(RarityFilter.onAverageOnceEvery(chance),heightRange);
    }
}
