package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.buuz135.industrial.block.tile.IndustrialProcessingTile;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import cy.jdkdigital.productivebees.common.crafting.ingredient.BeeIngredient;
import cy.jdkdigital.productivebees.common.recipe.BeeConversionRecipe;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BeeConverterBlockEntity extends IndustrialProcessingTile<BeeConverterBlockEntity> {
    @Save
    private final SidedInventoryComponent<BeeConverterBlockEntity> input1;
    @Save
    private final SidedInventoryComponent<BeeConverterBlockEntity> input2;
    @Save
    private final SidedInventoryComponent<BeeConverterBlockEntity> output;
    private BeeConversionRecipe recipe;
    public BeeConverterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.BEE_CONVERTER,84,41, blockPos, blockState);

        super.addInventory(this.input1 = (SidedInventoryComponent<BeeConverterBlockEntity>) new SidedInventoryComponent<BeeConverterBlockEntity>("input1",40,40,1,0).setColor(DyeColor.GREEN)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> !ProductiveBeesHelper.isBeeEgg(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
        super.addInventory(this.input2 = (SidedInventoryComponent<BeeConverterBlockEntity>) new SidedInventoryComponent<BeeConverterBlockEntity>("input2",62,40,1,1).setColor(DyeColor.BLUE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> ProductiveBeesHelper.isBeeEgg(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.output = (SidedInventoryComponent<BeeConverterBlockEntity>) new SidedInventoryComponent<BeeConverterBlockEntity>("output",113,40,1,2).setColor(DyeColor.RED)
                .setInputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
    }

    public void checkForRecipe(){
        if(this.level != null && isServer()){
            if(recipe != null && ProductiveBeesHelper.matchBeeConversionRecipe(recipe,level,this.input2.getStackInSlot(0),this.input1.getStackInSlot(0))){
                return;
            }

            recipe = RecipeUtil.getRecipes(this.level, ModRecipeTypes.BEE_CONVERSION_TYPE.get()).stream().filter(recipe -> ProductiveBeesHelper.matchBeeConversionRecipe(recipe,level,this.input2.getStackInSlot(0),this.input1.getStackInSlot(0))).findFirst().orElse(null);
        }
    }

    @Override
    public boolean canIncrease() {
        if(recipe != null){
            boolean hasEmptySlot = false;
            for (int i = 0; i < this.output.getSlots(); i++) {
                hasEmptySlot = this.output.getStackInSlot(i).isEmpty();
                if(!hasEmptySlot){
                    hasEmptySlot = ItemStack.isSameItemSameComponents(this.output.getStackInSlot(i), Objects.requireNonNull(ProductiveBeesHelper.beeEggFromEntity(recipe.result.get())));
                }
                if(hasEmptySlot){
                    break;
                }
            }
            return ProductiveBeesHelper.matchBeeConversionRecipe(recipe,level,this.input2.getStackInSlot(0),this.input1.getStackInSlot(0)) && hasEmptySlot;
        }
        return false;
    }

    @Override
    public Runnable onFinish() {
        return () ->{
            BeeConversionRecipe beeConversionRecipe = recipe;
            BeeIngredient beeIngredient = beeConversionRecipe.result.get();
            if(beeIngredient != null){
                ItemStack stack = ProductiveBeesHelper.beeEggFromEntity(beeIngredient);
                if(stack != null){
                    ItemHandlerHelper.insertItem(this.output,stack,false);
                    this.input1.getStackInSlot(0).shrink(1);
                    this.input2.getStackInSlot(0).shrink(1);
                }
            }
            this.checkForRecipe();
        };
    }

    @Override
    protected int getTickPower() {
        return 10000;
    }

    @Override
    protected @NotNull EnergyStorageComponent<BeeConverterBlockEntity> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000000, 4, 14);
    }

    @Override
    public @NotNull BeeConverterBlockEntity getSelf() {
        return this;
    }
}
