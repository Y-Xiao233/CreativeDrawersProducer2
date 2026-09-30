package net.yxiao233.cdp2.api.fluid;

import com.hrznstudio.titanium.module.DeferredRegistryHelper;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.yxiao233.cdp2.common.registry.CDPTab;
import net.yxiao233.industrialforegoingextra.IndustrialForegoingExtra;
import org.jetbrains.annotations.NotNull;

public class BaseFluidInstance {
    private final DeferredHolder<FluidType, FluidType> fluidType;
    private final DeferredHolder<Fluid, Fluid> flowingFluid;
    private final DeferredHolder<Fluid, Fluid> sourceFluid;
    private final DeferredHolder<Item, Item> bucketFluid;
    private final DeferredHolder<Block, Block> blockFluid;
    private final String fluidName;

    @SuppressWarnings("removal")
    public BaseFluidInstance(DeferredRegistryHelper helper, String name, FluidType.Properties fluidTypeProperties, IClientFluidTypeExtensions renderProperties) {
        this.fluidName = name;
        this.fluidType = helper.registerGeneric(NeoForgeRegistries.FLUID_TYPES.key(), name, () -> new FluidType(fluidTypeProperties) {
            public void initializeClient(@NotNull Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(renderProperties);
            }
        });
        this.sourceFluid = helper.registerGeneric(Registries.FLUID, name, () -> new BaseFluidInstance.Source(this));
        this.flowingFluid = helper.registerGeneric(Registries.FLUID, name + "_flowing", () -> new Flowing(this));
        this.blockFluid = helper.registerGeneric(Registries.BLOCK, name, () -> new LiquidBlock((FlowingFluid)this.sourceFluid.get(), Properties.of().replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).liquid().sound(SoundType.EMPTY).noLootTable()));
        this.bucketFluid = helper.registerGeneric(Registries.ITEM, name + "_bucket", () -> {
            BucketItem item = new BucketItem((Fluid)this.sourceFluid.get(), (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1));
            return item;
        });
    }

    public DeferredHolder<FluidType, FluidType> getFluidType() {
        return this.fluidType;
    }

    public DeferredHolder<Fluid, Fluid> getFlowingFluid() {
        return this.flowingFluid;
    }

    public DeferredHolder<Fluid, Fluid> getSourceFluid() {
        return this.sourceFluid;
    }

    public Item getBucketFluid() {
        return (Item)this.bucketFluid.get();
    }

    public Block getBlockFluid() {
        return (Block)this.blockFluid.get();
    }

    public String getFluidName() {
        return this.fluidName;
    }

    public static class Source extends BaseFluid {
        public Source(BaseFluidInstance instance) {
            super(instance);
        }

        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

        public boolean isSource(@NotNull FluidState state) {
            return true;
        }
    }

    public static class Flowing extends BaseFluid {
        public Flowing(BaseFluidInstance instance) {
            super(instance);
            this.registerDefaultState((FluidState)((FluidState)this.getStateDefinition().any()).setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new Property[]{LEVEL});
        }

        public int getAmount(@NotNull FluidState state) {
            return (Integer)state.getValue(LEVEL);
        }

        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }
}
