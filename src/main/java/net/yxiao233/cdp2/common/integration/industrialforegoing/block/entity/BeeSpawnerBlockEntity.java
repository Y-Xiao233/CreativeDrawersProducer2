package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.buuz135.industrial.block.tile.IndustrialProcessingTile;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import cy.jdkdigital.productivebees.common.block.NetherBeeNest;
import cy.jdkdigital.productivebees.common.block.SolitaryNest;
import cy.jdkdigital.productivebees.common.block.SugarbagNest;
import cy.jdkdigital.productivebees.common.recipe.BeeSpawningRecipe;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

public class BeeSpawnerBlockEntity extends IndustrialProcessingTile<BeeSpawnerBlockEntity> {
    @Save
    private final SidedInventoryComponent<BeeSpawnerBlockEntity> input;
    @Save
    private final SidedInventoryComponent<BeeSpawnerBlockEntity> nest;
    @Save
    private final SidedInventoryComponent<BeeSpawnerBlockEntity> output;
    private BeeSpawningRecipe recipe;
    public BeeSpawnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.BEE_SPAWNER, 74, 41, blockPos, blockState);
        super.addInventory(this.input = (SidedInventoryComponent<BeeSpawnerBlockEntity>) new SidedInventoryComponent<BeeSpawnerBlockEntity>("input",30,40,1,0).setColor(DyeColor.GREEN)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> !isNest(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.nest = (SidedInventoryComponent<BeeSpawnerBlockEntity>) new SidedInventoryComponent<BeeSpawnerBlockEntity>("nest",52,40,1,1).setColor(DyeColor.BLUE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> isNest(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.output = (SidedInventoryComponent<BeeSpawnerBlockEntity>) new SidedInventoryComponent<BeeSpawnerBlockEntity>("output",103,22,9,2).setColor(DyeColor.LIGHT_BLUE)
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

    public void checkForRecipe(){
        if(this.level != null && isServer()){
            if(recipe != null && ProductiveBeesHelper.matchBeeSpawningRecipe(recipe,nest.getStackInSlot(0),input.getStackInSlot(0))){
                return;
            }

            recipe = RecipeUtil.getRecipes(this.level, ModRecipeTypes.BEE_SPAWNING_TYPE.get()).stream().filter(recipe -> ProductiveBeesHelper.matchBeeSpawningRecipe(recipe,nest.getStackInSlot(0),input.getStackInSlot(0))).findFirst().orElse(null);
        }
    }

    @Override
    public boolean canIncrease() {
        if(recipe != null){
            boolean hasEmptySlot = false;
            for (int i = 0; i < this.output.getSlots(); i++) {
                hasEmptySlot = this.output.getStackInSlot(i).isEmpty();
                if(hasEmptySlot){
                    break;
                }
            }
            return ProductiveBeesHelper.matchBeeSpawningRecipe(recipe,nest.getStackInSlot(0),input.getStackInSlot(0)) && hasEmptySlot;
        }
        return false;
    }

    public boolean isNest(ItemStack stack){
        if(!(stack.getItem() instanceof BlockItem item)){
            return false;
        }
        Block block = Block.byItem(item);
        if(block instanceof SolitaryNest){
            return true;
        }else if(block instanceof NetherBeeNest){
            return true;
        }else{
            return block instanceof SugarbagNest;
        }
    }

    @Override
    public Runnable onFinish() {
        return () ->{
            BeeSpawningRecipe beeSpawningRecipe = recipe;
            ItemStack output = ProductiveBeesHelper.getBeeSpawningRecipeOutput(level, beeSpawningRecipe);
            if(output != null){
                ItemHandlerHelper.insertItem(this.output,output,false);
                this.input.getStackInSlot(0).shrink(1);
            }
            this.checkForRecipe();
        };
    }

    @Override
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }

    @Override
    protected int getTickPower() {
        return 10000;
    }

    @Override
    protected @NotNull EnergyStorageComponent<BeeSpawnerBlockEntity> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000000, 4, 14);
    }

    @Override
    public @NotNull BeeSpawnerBlockEntity getSelf() {
        return this;
    }
}
