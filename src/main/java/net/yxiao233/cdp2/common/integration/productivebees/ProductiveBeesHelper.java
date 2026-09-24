package net.yxiao233.cdp2.common.integration.productivebees;

import com.hrznstudio.titanium.util.RecipeUtil;
import cy.jdkdigital.productivebees.common.crafting.ingredient.BeeIngredient;
import cy.jdkdigital.productivebees.common.item.SpawnEgg;
import cy.jdkdigital.productivebees.common.recipe.BeeBreedingRecipe;
import cy.jdkdigital.productivebees.common.recipe.BeeConversionRecipe;
import cy.jdkdigital.productivebees.common.recipe.BeeFishingRecipe;
import cy.jdkdigital.productivebees.common.recipe.BeeSpawningRecipe;
import cy.jdkdigital.productivebees.init.ModRecipeTypes;
import cy.jdkdigital.productivebees.util.BeeHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ProductiveBeesHelper {
    public static ItemStack beeEggFromEntity(EntityType<? extends Entity> entityType){
        return beeEggFromKey(BuiltInRegistries.ENTITY_TYPE.getKey(entityType));
    }

    public static ItemStack beeEggFromEntity(BeeIngredient beeIngredient){
        if(!beeIngredient.isConfigurable()){
            return beeEggFromEntity(beeIngredient.getBeeEntity());
        }
        ResourceLocation entityKey = beeIngredient.getBeeType();
        if(entityKey.equals(BuiltInRegistries.ENTITY_TYPE.getDefaultKey())){
            return null;
        }
        ResourceLocation configurableKey = ResourceLocation.parse("productivebees:spawn_egg_configurable_bee");
        Item configurableItem = BuiltInRegistries.ITEM.get(configurableKey);
        ItemStack stack = configurableItem.getDefaultInstance().copy();
        CompoundTag entityData = new CompoundTag();
        entityData.putString("id","productivebees:configurable_bee");
        entityData.putString("type",entityKey.toString());
        stack.set(DataComponents.ENTITY_DATA, CustomData.of(entityData));
        return stack.copy();
    }

    public static ItemStack beeEggFromKey(ResourceLocation beeKey){
        if(beeKey.equals(BuiltInRegistries.ENTITY_TYPE.getDefaultKey())){
            return null;
        }
        ResourceLocation itemKey = ResourceLocation.fromNamespaceAndPath(beeKey.getNamespace(), "spawn_egg_" + beeKey.getPath());
        Item item = BuiltInRegistries.ITEM.get(itemKey);
        if(item == Items.AIR){
            return null;
        }
        return item.getDefaultInstance().copy();
    }

    public static boolean isBeeEgg(ItemStack stack){
        return stack.getItem() instanceof SpawnEgg;
    }

    public static EntityType<? extends Entity> entityTypeFromBeeEgg(ItemStack stack){
        if(!(stack.getItem() instanceof SpawnEgg egg)){
            return null;
        }
        return egg.getType(stack);
    }

    @SuppressWarnings("all")
    public static String getBeeType(ItemStack stack){
        EntityType<? extends Entity> entityType = entityTypeFromBeeEgg(stack);
        if(entityType != null){
            ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
            if(key.equals(ResourceLocation.parse("productivebees:configurable_bee")) && stack.has(DataComponents.ENTITY_DATA)){
                return stack.get(DataComponents.ENTITY_DATA).copyTag().getString("type");
            }
            return key.toString();
        }
        return null;
    }

    public static String getBeeType(EntityType<? extends Bee> entityType){
        if(entityType != null){
            return BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString();
        }
        return null;
    }

    public static boolean matchBreedingRecipe(BeeBreedingRecipe recipe, Level level, ItemStack stack1, ItemStack stack2){
        return recipe.matches(new BeeHelper.IdentifierInventory(getBeeType(stack1),getBeeType(stack2)),level);
    }

    public static boolean matchBeeSpawningRecipe(BeeSpawningRecipe recipe, ItemStack nest, ItemStack heldItem){
        return recipe.ingredient.test(nest) && recipe.spawnItem.test(heldItem);
    }

    public static List<BeeSpawningRecipe> getBeeSpawningRecipesMatched(Level level, ItemStack nest, ItemStack heldItem){
        return List.copyOf(RecipeUtil.getRecipes(level, ModRecipeTypes.BEE_SPAWNING_TYPE.get()).stream().filter(recipe -> matchBeeSpawningRecipe(recipe, nest, heldItem)).toList());
    }

    public static ItemStack getBeeSpawningRecipeOutput(Level level, BeeSpawningRecipe recipe){
        List<BeeSpawningRecipe> beeSpawningRecipesMatched = getBeeSpawningRecipesMatched(level, recipe.ingredient.getItems()[0].copy(), recipe.spawnItem.getItems()[0].copy());
        List<BeeIngredient> bees = new ArrayList<>();
        beeSpawningRecipesMatched.forEach(matched -> {
            bees.addAll(matched.output.stream().map(Supplier::get).toList());
        });
        return beeEggFromEntity(bees.get(level.getRandom().nextInt(bees.size())));
    }

    public static ItemStack getBeeFishingRecipeOutput(Level level){
        List<BeeFishingRecipe> recipes = RecipeUtil.getRecipes(level, ModRecipeTypes.BEE_FISHING_TYPE.get());
        List<BeeIngredient> bees = new ArrayList<>();
        recipes.forEach(recipe ->{
            bees.add(recipe.output.get());
        });
        return beeEggFromEntity(bees.get(level.getRandom().nextInt(bees.size())));
    }

    public static boolean matchBeeConversionRecipe(BeeConversionRecipe recipe, Level level, ItemStack bee, ItemStack item){
        return recipe.matches(new BeeHelper.IdentifierInventory(getBeeType(bee),BuiltInRegistries.ITEM.getKey(item.getItem()).toString()),level);
    }
}
