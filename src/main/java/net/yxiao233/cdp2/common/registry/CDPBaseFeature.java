package net.yxiao233.cdp2.common.registry;

import com.sammy.malum.registry.common.block.MalumBlocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.yxiao233.cdp2.api.registry.CDPBlockDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPConfigureFeatureRegister;
import net.yxiao233.cdp2.api.registry.CDPDefaultFeatureRegister;
import net.yxiao233.cdp2.worldgen.CDPOrePlacement;

import java.util.List;

public class CDPBaseFeature {
    /** The unknown dimension's underground is malum:twisted_rock, so the ores replace it (like the overworld gold ore replaces #minecraft:stone_ore_replaceables) */
    public static final RuleTest TWISTED_ROCK = new BlockMatchTest(MalumBlocks.TWISTED_ROCK.get());
    /** Same as the overworld gold ore: 4 veins per chunk, trapezoid between Y -64 and Y 32 */
    private static final List<PlacementModifier> GOLD_ORE_PLACEMENT = CDPOrePlacement.commonOrePlacement(4,HeightRangePlacement.triangle(VerticalAnchor.absolute(-64),VerticalAnchor.absolute(32)));

    public static final CDPDefaultFeatureRegister TWISTED_CTHONIC_GOLD_ORE = ore("twisted_cthonic_gold_ore",CDPBlock.TWISTED_CTHONIC_GOLD_ORE);
    public static final CDPDefaultFeatureRegister TWISTED_SOULSTONE_ORE = ore("twisted_soulstone_ore",CDPBlock.TWISTED_SOULSTONE_ORE);
    public static final CDPDefaultFeatureRegister TWISTED_BRILLIANT_STONE = ore("twisted_brilliant_stone",CDPBlock.TWISTED_BRILLIANT_STONE);

    public static void init(){}

    /** Copies the overworld gold ore generation (vein size 9) into every biome of the unknown dimension */
    private static CDPDefaultFeatureRegister ore(String name, CDPBlockDeferredRegister block){
        List<OreConfiguration.TargetBlockState> rules = List.of(CDPConfigureFeatureRegister.rule(TWISTED_ROCK,block.getBlock()));
        return CDPDefaultFeatureRegister.registry(name,CDPBiomes.UNKNOWN_BIOMES,rules,9,GOLD_ORE_PLACEMENT);
    }
}
