package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.buuz135.industrial.block.tile.IndustrialProcessingTile;
import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.energy.EnergyStorageComponent;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import cy.jdkdigital.productivebees.common.crafting.ingredient.BeeIngredient;
import cy.jdkdigital.productivebees.common.recipe.BeeBreedingRecipe;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.common.integration.productivebees.ProductiveBeesHelper;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BreedingChamberBlockEntity extends IndustrialProcessingTile<BreedingChamberBlockEntity> {
    @Save
    private final SidedInventoryComponent<BreedingChamberBlockEntity> input1;
    @Save
    private final SidedInventoryComponent<BreedingChamberBlockEntity> input2;
    @Save
    private final SidedInventoryComponent<BreedingChamberBlockEntity> output;
    private List<BeeBreedingRecipe> recipes;
    public BreedingChamberBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.BREEDING_CHAMBER, 84, 41, blockPos, blockState);

        super.addInventory(this.input1 = (SidedInventoryComponent<BreedingChamberBlockEntity>) new SidedInventoryComponent<BreedingChamberBlockEntity>("input1",40,40,1,0).setColor(DyeColor.GREEN)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> canInsert(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
        super.addInventory(this.input2 = (SidedInventoryComponent<BreedingChamberBlockEntity>) new SidedInventoryComponent<BreedingChamberBlockEntity>("input2",62,40,1,1).setColor(DyeColor.BLUE)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> canInsert(itemStack))
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.output = (SidedInventoryComponent<BreedingChamberBlockEntity>) new SidedInventoryComponent<BreedingChamberBlockEntity>("output",113,40,1,2).setColor(DyeColor.RED)
                .setInputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );
    }

    public void checkForRecipe() {
        if (this.level != null && isServer()) {
            if (recipes != null && !recipes.isEmpty() && ProductiveBeesHelper.matchBreedingRecipe(recipes.getFirst(), level, this.input1.getStackInSlot(0), this.input2.getStackInSlot(0))) {
                return;
            }

            recipes = RecipeUtil.getRecipes(this.level, ModRecipeTypes.BEE_BREEDING_TYPE.get()).stream().filter(recipe -> ProductiveBeesHelper.matchBreedingRecipe(recipe, level, this.input1.getStackInSlot(0), this.input2.getStackInSlot(0))).toList();
        }
    }

    @Override
    public boolean canIncrease() {
        if(recipes != null && !recipes.isEmpty()){
            boolean hasEmptySlot = false;
            for (int i = 0; i < this.output.getSlots(); i++) {
                hasEmptySlot = this.output.getStackInSlot(i).isEmpty();
                if(hasEmptySlot){
                    break;
                }
            }
            return ProductiveBeesHelper.matchBreedingRecipe(recipes.getFirst(),level,this.input1.getStackInSlot(0),this.input2.getStackInSlot(0)) && hasEmptySlot;
        }

        ItemStack stackInSlot1 = this.input1.getStackInSlot(0);
        ItemStack stackInSlot2 = this.input2.getStackInSlot(0);
        if(ItemStack.isSameItemSameComponents(stackInSlot1,stackInSlot2) && !stackInSlot1.isEmpty()){
            return this.output.getStackInSlot(0).isEmpty() || ItemStack.isSameItemSameComponents(this.input1.getStackInSlot(0),this.output.getStackInSlot(0));
        }
        return false;
    }

    public boolean canInsert(ItemStack stack){
        return ProductiveBeesHelper.isBeeEgg(stack);
    }

    @Override
    public Runnable onFinish() {
        return () -> {
            ItemStack stackInSlot1 = this.input1.getStackInSlot(0).copy();
            ItemStack stackInSlot2 = this.input2.getStackInSlot(0).copy();
            if((recipes == null || recipes.isEmpty()) && ItemStack.isSameItemSameComponents(stackInSlot1,stackInSlot2) && !stackInSlot1.isEmpty()){
                ItemHandlerHelper.insertItem(this.output,stackInSlot1,false);
            }else{
                List<BeeBreedingRecipe> recipeList = List.copyOf(recipes);
                BeeBreedingRecipe beeBreedingRecipe = recipeList.get(level.getRandom().nextInt(recipes.size()));
                BeeIngredient beeIngredient = beeBreedingRecipe.offspring.get();
                if(beeIngredient != null){
                    ItemStack stack = ProductiveBeesHelper.beeEggFromEntity(beeIngredient);
                    if(stack != null){
                        ItemHandlerHelper.insertItem(this.output,stack,false);
                    }
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
    public void setChanged() {
        super.setChanged();
        checkForRecipe();
    }

    @Override
    protected @NotNull EnergyStorageComponent<BreedingChamberBlockEntity> createEnergyStorage() {
        return new EnergyStorageComponent<>(10000000, 4, 14);
    }

    @Override
    public @NotNull BreedingChamberBlockEntity getSelf() {
        return this;
    }
}
