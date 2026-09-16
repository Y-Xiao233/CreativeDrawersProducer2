package net.yxiao233.cdp2.api.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

import java.util.ArrayList;
import java.util.List;

public class CDPConfigureFeatureRegister {
    private static final ArrayList<CDPConfigureFeatureRegister> FEATURES = new ArrayList<>();
    private final ResourceKey<ConfiguredFeature<?,?>> configuredFeature;
    private final List<OreConfiguration.TargetBlockState> rules;
    private final int size;

    public static final RuleTest STONE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
    public static final RuleTest DEEPSLATE = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

    private CDPConfigureFeatureRegister(ResourceKey<ConfiguredFeature<?,?>> configuredFeature, List<OreConfiguration.TargetBlockState> rules, int size){
        this.configuredFeature = configuredFeature;
        this.rules = rules;
        this.size = size;
        FEATURES.add(this);
    }
    public static CDPConfigureFeatureRegister registrySimple(String name, int size, List<OreConfiguration.TargetBlockState> rules){
        ResourceKey<ConfiguredFeature<?,?>> key = registryKey(name);
        return new CDPConfigureFeatureRegister(key,rules,size);
    }

    public static void registry(BootstrapContext<ConfiguredFeature<?,?>> context){
        FEATURES.forEach(holder -> registry(context,holder.configuredFeature, Feature.ORE,new OreConfiguration(holder.rules, holder.size)));
    }
    private static ResourceKey<ConfiguredFeature<?,?>> registryKey(String name){
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, CreativeDrawersProducer2.makeId(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registry(BootstrapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature,configuration));
    }

    public static OreConfiguration.TargetBlockState stoneRule(BlockState state){
        return rule(STONE,state);
    }

    public static OreConfiguration.TargetBlockState deepslateRule(BlockState state){
        return rule(DEEPSLATE,state);
    }

    public static OreConfiguration.TargetBlockState rule(RuleTest rule, BlockState state){
        return OreConfiguration.target(rule,state);
    }

    public static OreConfiguration.TargetBlockState rule(RuleTest rule, DeferredHolder<Block,Block> block){
        return OreConfiguration.target(rule,block.get().defaultBlockState());
    }
    public ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature() {
        return configuredFeature;
    }
}
