package net.yxiao233.cdp2.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
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
import net.yxiao233.cdp2.common.block.entity.CreativeDrawerBlockEntity;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPItem;

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
}
