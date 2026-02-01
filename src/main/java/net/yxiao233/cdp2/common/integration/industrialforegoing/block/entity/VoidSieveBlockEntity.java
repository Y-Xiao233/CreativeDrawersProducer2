package net.yxiao233.cdp2.common.integration.industrialforegoing.block.entity;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.component.inventory.SidedInventoryComponent;
import com.hrznstudio.titanium.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.api.block.entity.VoidProcessingTile;
import net.yxiao233.cdp2.common.recipe.VoidSieveRecipe;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import org.jetbrains.annotations.NotNull;

public class VoidSieveBlockEntity extends VoidProcessingTile<VoidSieveBlockEntity>{
    @Save
    private SidedInventoryComponent<VoidSieveBlockEntity> input;
    @Save
    private SidedInventoryComponent<VoidSieveBlockEntity> output;
    private VoidSieveRecipe recipe;
    public VoidSieveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(CDPBlock.VOID_SIEVE, 68,41, blockPos, blockState);

        super.addInventory(this.input = (SidedInventoryComponent<VoidSieveBlockEntity>) new SidedInventoryComponent<VoidSieveBlockEntity>("input",43,40,1,1).setColor(DyeColor.GREEN)
                .setOnSlotChanged((stack, integer) -> checkForRecipe())
                .setInputFilter((itemStack, integer) -> !canIncrease())
                .setOutputFilter((stack, integer) -> false)
                .setComponentHarness(this)
        );

        super.addInventory(this.output = (SidedInventoryComponent<VoidSieveBlockEntity>) new SidedInventoryComponent<VoidSieveBlockEntity>("output",103,22,9,2).setColor(DyeColor.LIGHT_BLUE)
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
                .setInputFilter((stack, integer) -> {
                    return false;
                }).setComponentHarness(this)
        );
    }

    @NotNull
    @Override
    public VoidSieveBlockEntity getSelf() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void checkForRecipe(){
        if(this.level != null && isServer()){
            if(recipe != null && recipe.matches(input, output)){
                return;
            }

            recipe = RecipeUtil.getRecipes(this.level,(RecipeType<VoidSieveRecipe>) CDPRecipe.VOID_SIEVE_TYPE.get()).stream().filter(recipe -> recipe.matches(input,output)).findFirst().orElse(null);
        }
    }

    @Override
    public boolean canIncrease() {
        if(recipe != null){
            return recipe.input.sizedIngredient().test(this.input.getStackInSlot(0));
        }
        return false;
    }


    @Override
    public Runnable onFinish() {
        return () ->{
            VoidSieveRecipe sieveRecipe = recipe;
            sieveRecipe.outputs.forEach(output ->{
                if(level != null && level.getRandom().nextFloat() <= output.chance()){
                    ItemHandlerHelper.insertItem(this.output,output.item().copy(),false);
                }
            });
            if(level != null && level.getRandom().nextFloat() <= sieveRecipe.input.chance()){
                this.input.getStackInSlot(0).shrink(sieveRecipe.input.sizedIngredient().count());
            }
            this.checkForRecipe();
        };
    }

    public ItemStack getBlockForDisplay(){
        return this.input.getStackInSlot(0);
    }

    public boolean shouldDisplay(){
        return !getBlockForDisplay().isEmpty() && getBlockForDisplay().getItem() instanceof BlockItem && progress < getMaxProgress() && voidMatterCount >= this.getTickMatter();
    }
    @Override
    protected int getTickMatter() {
        return 10;
    }

    @Override
    public int getEachVoidMatterValue() {
        return 2;
    }

    @Override
    public int getMaxProgress() {
        return 10;
    }
}
