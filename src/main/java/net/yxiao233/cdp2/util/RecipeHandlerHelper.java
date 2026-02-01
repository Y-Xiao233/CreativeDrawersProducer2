package net.yxiao233.cdp2.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeHandlerHelper {
    public static boolean insert(IItemHandler handler, List<ChanceItemStack> stacks, boolean simulate){
        if(handler != null){
            ItemStackHandler instance = new ItemStackHandler(handler.getSlots());
            for (int i = 0; i < handler.getSlots(); i++) {
                instance.setStackInSlot(i,handler.getStackInSlot(i));
            }

            AtomicBoolean canInsert = new AtomicBoolean(false);
            for(ChanceItemStack chanceStack : stacks){
                canInsert.set(ItemHandlerHelper.insertItem(instance,chanceStack.item(),false).isEmpty());
                if(!canInsert.get()){
                    break;
                }
            }

            if(!simulate && canInsert.get()){
                stacks.forEach(chanceStack ->{
                    ItemHandlerHelper.insertItem(handler,chanceStack.item(),false);
                });
            }

            return canInsert.get();
        }
        return stacks.isEmpty();
    }

    public static boolean extract(IItemHandler handler, List<ChanceIngredient> inputs, boolean simulate){
        if(handler == null){
            return false;
        }
        if(inputs.isEmpty()){
            return true;
        }

        int has = 0;
        ItemStackHandler instance = new ItemStackHandler(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i++) {
            instance.setStackInSlot(i,handler.getStackInSlot(i));
        }

        for (int i = 0; i < instance.getSlots(); i++) {
            for (ChanceIngredient input : inputs) {
                Ingredient ingredient = input.sizedIngredient().ingredient();
                for(ItemStack stack : ingredient.getItems()){
                    if(ItemStack.isSameItemSameComponents(instance.getStackInSlot(i),stack) && instance.getStackInSlot(i).getCount() >= stack.getCount()){
                        instance.extractItem(i,stack.getCount(),true);
                        if(!simulate){
                            handler.extractItem(i,stack.getCount(),false);
                        }
                        has ++;
                    }
                }
            }
        }

        return has == inputs.size();
    }

    public static boolean extract(IItemHandler handler, ChanceIngredient ingredient, boolean simulate){
        return extract(handler,List.of(ingredient),simulate);
    }

    public static boolean extract(IItemHandler handler, SizedIngredient sizedIngredient, boolean simulate){
        return extract(handler,List.of(new ChanceIngredient(sizedIngredient,1)),simulate);
    }
}
