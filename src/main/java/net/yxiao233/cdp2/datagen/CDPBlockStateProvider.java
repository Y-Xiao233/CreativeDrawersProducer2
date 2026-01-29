package net.yxiao233.cdp2.datagen;

import com.blakebr0.mysticalagriculture.block.InfusedFarmlandBlock;
import net.darkhax.botanypots.common.impl.block.PotType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.block.property.IRotatableBlock;
import net.yxiao233.cdp2.api.block.property.RotationHandler;
import net.yxiao233.cdp2.api.registry.CDPBlockDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;
import net.yxiao233.cdp2.common.block.CreativeDrawerBlock;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;
import net.yxiao233.cdp2.integration.botanypot.CDPBotanyPotEntityBlock;

public class CDPBlockStateProvider extends BlockStateProvider {
    public CDPBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreativeDrawersProducer2.MODID,exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        CDPBlock.CREATIVE_DRAWERS_MAP.forEach((location, register) -> {
            builtinEntityItem(register);
            drawerBlockState(register);
        });
        builtinEntityItem(CDPItem.RANDOM_CREATIVE_DRAWER);

        fourWayBlockState(CDPBlock.UPGRADE_STATION);
        onlyItem(CDPBlock.UPGRADE_STATION);

        infusedFarmlandBlockState(CDPBlock.ABSOLUTE_FARMLAND);
        infusedFarmlandBlockState(CDPBlock.SUPREME_FARMLAND);
        infusedFarmlandBlockState(CDPBlock.COSMIC_FARMLAND);
        onlyItem(CDPBlock.INFINITE_FARMLAND);

        botanyPots();
    }

    private void cubeAll(DeferredHolder<Block,Block> registryObject){
        simpleBlockWithItem(registryObject.get(),cubeAll(registryObject.get()));
    }

    private void cubeAll(CDPBlockDeferredRegister register){
        cubeAll(register.getBlock());
    }

    private void onlyItem(DeferredHolder<Block,Block> registryObject){
        simpleBlockItem(registryObject.get(),new ModelFile.UncheckedModelFile(CreativeDrawersProducer2.MODID +
                ":block/" + BuiltInRegistries.BLOCK.getKey(registryObject.get()).getPath()));
    }

    private void onlyItem(CDPBlockDeferredRegister register){
        onlyItem(register.getBlock());
    }

    private void onlyItem(CDPBlockEntityDeferredRegister<?> holder){
        onlyItem(holder.getBlockDeferredHolder());
    }

    private void builtinEntityItem(CDPBlockEntityDeferredRegister<?> register) {
        this.itemModels().getBuilder(BuiltInRegistries.BLOCK.getKey(register.asBlock()).getPath()).parent(new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath("minecraft", "builtin/entity"))
        );
    }

    private void builtinEntityItem(CDPItemDeferredRegister register) {
        this.itemModels().getBuilder(BuiltInRegistries.ITEM.getKey(register.asItem()).getPath()).parent(new ModelFile.UncheckedModelFile(
                ResourceLocation.fromNamespaceAndPath("minecraft", "builtin/entity"))
        );
    }

    private void fourWayBlockState(CDPBlockEntityDeferredRegister<?> register, String modelPath){
        if(register.asBlock() instanceof IRotatableBlock rotatableBlock) {
            fourWayBlockState((Block & IRotatableBlock) rotatableBlock, modelPath);
        }
    }

    private void fourWayBlockState(CDPBlockEntityDeferredRegister<?> register){
        fourWayBlockState(register,BuiltInRegistries.BLOCK.getKey(register.asBlock()).getPath());
    }
    private <T extends Block & IRotatableBlock> void fourWayBlockState(T block, String modelPath){
        fourWayBlockState(block,ResourceLocation.fromNamespaceAndPath(CreativeDrawersProducer2.MODID, "block/" + modelPath));
    }

    private <T extends Block & IRotatableBlock> void fourWayBlockState(T block, ResourceLocation modelPath){
        Property<Direction> property = RotationHandler.FACING_HORIZONTAL;
        getVariantBuilder(block)
                .partialState().with(property, Direction.NORTH)
                .modelForState()
                .modelFile(models().getExistingFile(modelPath))
                .rotationY(0)
                .addModel()

                .partialState().with(property, Direction.EAST)
                .modelForState()
                .modelFile(models().getExistingFile(modelPath))
                .rotationY(90)
                .addModel()

                .partialState().with(property, Direction.SOUTH)
                .modelForState()
                .modelFile(models().getExistingFile(modelPath))
                .rotationY(180)
                .addModel()

                .partialState().with(property, Direction.WEST)
                .modelForState()
                .modelFile(models().getExistingFile(modelPath))
                .rotationY(270)
                .addModel();
    }

    private void drawerBlockState(CDPBlockEntityDeferredRegister<?> register){
        if(register.asBlock() instanceof CreativeDrawerBlock drawerBlock){
            fourWayBlockState(drawerBlock,"creative_drawer");
        }
    }

    private void infusedFarmlandBlockState(CDPBlockDeferredRegister register){
        if(register.asBlock() instanceof InfusedFarmlandBlock infusedFarmlandBlock){
            IntegerProperty property = FarmBlock.MOISTURE;
            ResourceLocation path0 = ResourceLocation.parse("cdp2:block/infused_farmland");
            ResourceLocation path1 = ResourceLocation.parse("cdp2:block/infused_farmland_moist");
            getVariantBuilder(infusedFarmlandBlock)
                    .partialState().with(property, 0)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 1)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 2)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 3)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 4)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 5)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 6)
                    .modelForState()
                    .modelFile(models().getExistingFile(path0))
                    .addModel()

                    .partialState().with(property, 7)
                    .modelForState()
                    .modelFile(models().getExistingFile(path1))
                    .addModel();


            itemModels().getBuilder(BuiltInRegistries.ITEM.getKey(register.asItem()).toString()).parent(models().getExistingFile(ResourceLocation.parse("cdp2:block/infused_farmland")));
        }
    }


    private void botanyPots(){
        CDPBlock.POTS_MAP.forEach(((location, register) -> {
            if(register.asBlock() instanceof CDPBotanyPotEntityBlock pot){
                String prefix = pot.getPotType().equals(PotType.HOPPER) ? "hopper_pot" : "pot";
                models().getBuilder(location.toString())
                        .parent(models().getExistingFile(ResourceLocation.parse("cdp2:block/template/" + prefix)))
                        .renderType("minecraft:cutout")
                        .texture("material","minecraft:block/terracotta")
                        .texture("material_top","cdp2:block/terracotta_pot_top")
                        .texture("stripe","cdp2:block/" + pot.getPotTier().getName());
                getVariantBuilder(register.asBlock()).partialState().modelForState().modelFile(models().getExistingFile(ResourceLocation.parse("cdp2:block/" + location.getPath()))).addModel();
                itemModels().getBuilder(location.toString()).parent(models().getExistingFile(ResourceLocation.parse("cdp2:block/" + location.getPath())));
            }
        }));
    }
}
