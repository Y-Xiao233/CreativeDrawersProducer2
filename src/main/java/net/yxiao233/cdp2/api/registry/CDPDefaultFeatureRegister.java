package net.yxiao233.cdp2.api.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.yxiao233.cdp2.common.registry.CDPBiomes;

import java.util.List;

public class CDPDefaultFeatureRegister {
    private final CDPConfigureFeatureRegister configureFeatureHolder;
    private final CDPPlacedFeatureRegister placedFeatureHolder;
    private final CDPBiomeModifiersRegister biomeModifiersHolder;
    private CDPDefaultFeatureRegister(CDPConfigureFeatureRegister configureFeatureHolder, CDPPlacedFeatureRegister placedFeatureHolder, CDPBiomeModifiersRegister biomeModifiersHolder){
        this.configureFeatureHolder = configureFeatureHolder;
        this.placedFeatureHolder = placedFeatureHolder;
        this.biomeModifiersHolder = biomeModifiersHolder;
    }

    public static CDPDefaultFeatureRegister registry(String name, CDPBlockDeferredRegister replaceBlock, ResourceKey<Biome> biome){
        return registry(name,replaceBlock,biome,5,-64,80);
    }
    public static CDPDefaultFeatureRegister registry(String name, CDPBlockDeferredRegister replaceBlock, ResourceKey<Biome> biome, int count, int min, int max){
        return registry(name,biome,List.of(CDPConfigureFeatureRegister.stoneRule(replaceBlock.asBlockState())),36,count,min,max);
    }

    public static CDPDefaultFeatureRegister registry(String name, ResourceKey<Biome> biome, List<OreConfiguration.TargetBlockState> rules, int size, int count, int min, int max){
        CDPConfigureFeatureRegister c = configureFeature(name,rules,size);
        CDPPlacedFeatureRegister p = placedFeature(name,c,count,min,max);
        CDPBiomeModifiersRegister b = defaultBiomeModifiers(name,biome,p);
        return new CDPDefaultFeatureRegister(c,p,b);
    }
    public static CDPDefaultFeatureRegister registryDefault(String name, CDPBlockDeferredRegister replaceBlock){
        return registry(name,replaceBlock, CDPBiomes.QUORVETH);
    }

    public static CDPDefaultFeatureRegister registryDefault(CDPBlockDeferredRegister replaceBlock){
        String name = replaceBlock.getBlock().getId().getPath();
        return registryDefault(name,replaceBlock);
    }

    private static CDPConfigureFeatureRegister configureFeature(String name, List<OreConfiguration.TargetBlockState> rules, int size){
        return CDPConfigureFeatureRegister.registrySimple(name, size, rules);
    }
    private static CDPPlacedFeatureRegister placedFeature(String name, CDPConfigureFeatureRegister key, int count , int min, int max){
        return CDPPlacedFeatureRegister.registrySimple(name + "_placed", key.getConfiguredFeature(),count,min,max);
    }

    private static CDPBiomeModifiersRegister defaultBiomeModifiers(String name, ResourceKey<Biome> biome, CDPPlacedFeatureRegister placedKey){
        return CDPBiomeModifiersRegister.registrySimple("add_" + name, biome, placedKey.getPlacedFeature());
    }

    public CDPConfigureFeatureRegister getConfigureFeatureHolder() {
        return configureFeatureHolder;
    }

    public CDPPlacedFeatureRegister getPlacedFeatureHolder() {
        return placedFeatureHolder;
    }

    public CDPBiomeModifiersRegister getBiomeModifiersHolder() {
        return biomeModifiersHolder;
    }
}
