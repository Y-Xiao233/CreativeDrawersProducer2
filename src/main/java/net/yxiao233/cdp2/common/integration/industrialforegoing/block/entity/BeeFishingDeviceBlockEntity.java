package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.buuz135.industrial.block.tile.IndustrialProcessingTile;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

public class BeeFishingDeviceBlockEntity extends IndustrialProcessingTile<BeeFishingDeviceBlockEntity> {
    @Save
    private final SidedInventoryComponent<BeeFishingDeviceBlockEntity> input;
    @Save
    private final SidedInventoryComponent<BeeFishingDeviceBlockEntity> output;
    public BeeFishingDeviceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.BEE_FISHING_DEVICE, 74, 41, blockPos, blockState);

        super.addInventory(this.input = (SidedInventoryComponent<BeeFishingDeviceBlockEntity>) new SidedInventoryComponent<BeeFishingDeviceBlockEntity>("input",52,40,1,0).setColor(DyeColor.GREEN)
                .setInputFilter((itemStack, integer) -> canInsert(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.output = (SidedInventoryComponent<BeeFishingDeviceBlockEntity>) new SidedInventoryComponent<BeeFishingDeviceBlockEntity>("output",103,22,9,1).setColor(DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(0, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(1, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(2, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(3, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(4, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(5, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(6, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(7, DyeColor.LIGHT_BLUE)
                .setSlotToColorRender(8, DyeColor.LIGHT_BLUE)
                .setRange(3, 4)
                .setInputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
    }

    public boolean canInsert(ItemStack stack){
        return stack.is(Items.FISHING_ROD);
    }

    @Override
    public boolean canIncrease() {
        if(!this.input.getStackInSlot(0).is(Items.FISHING_ROD)){
            return false;
        }
        boolean hasEmptySlot = false;
        for (int i = 0; i < this.output.getSlots(); i++) {
            hasEmptySlot = this.output.getStackInSlot(i).isEmpty();
            if(hasEmptySlot){
                break;
            }
        }
        return hasEmptySlot;
    }

    @Override
    public Runnable onFinish() {
        return () ->{
            ItemStack output = ProductiveBeesHelper.getBeeFishingRecipeOutput(level);
            if(output != null){
                ItemHandlerHelper.insertItem(this.output,output,false);
                this.input.getStackInSlot(0).shrink(1);
            }
        };
    }

    @Override
    protected int getTickPower() {
        return 10000;
    }

    @Override
    protected @NotNull EnergyStorageComponent<BeeFishingDeviceBlockEntity> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000000, 4, 14);
    }

    @Override
    public @NotNull BeeFishingDeviceBlockEntity getSelf() {
        return this;
    }
}
