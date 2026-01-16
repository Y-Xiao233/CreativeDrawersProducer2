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
import net.yxiao233.cdp2.api.block.property.RotationHandler;
import net.yxiao233.cdp2.api.registry.CDPBlockDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPBlockEntityDeferredRegister;
import net.yxiao233.cdp2.api.registry.CDPItemDeferredRegister;
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

    private void drawerBlockState(CDPBlockEntityDeferredRegister<?> register){
        Property<Direction> property = RotationHandler.FACING_HORIZONTAL;
        ResourceLocation modelLocation = ResourceLocation.fromNamespaceAndPath(CreativeDrawersProducer2.MODID, "block/creative_drawer");
        getVariantBuilder(register.asBlock())
                .partialState().with(property, Direction.NORTH)
                .modelForState()
                .modelFile(models().getExistingFile(modelLocation))
                .rotationY(0)
                .addModel()

                .partialState().with(property, Direction.EAST)
                .modelForState()
                .modelFile(models().getExistingFile(modelLocation))
                .rotationY(90)
                .addModel()

                .partialState().with(property, Direction.SOUTH)
                .modelForState()
                .modelFile(models().getExistingFile(modelLocation))
                .rotationY(180)
                .addModel()

                .partialState().with(property, Direction.WEST)
                .modelForState()
                .modelFile(models().getExistingFile(modelLocation))
                .rotationY(270)
                .addModel();
    }
}
