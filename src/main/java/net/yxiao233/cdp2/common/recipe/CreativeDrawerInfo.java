package net.yxiao233.cdp2.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.yxiao233.cdp2.api.recipe.BaseRecipe;
import net.yxiao233.cdp2.common.registry.CDPRecipe;
import org.jetbrains.annotations.NotNull;

public class CreativeDrawerInfo extends BaseRecipe {
    public static final MapCodec<CreativeDrawerInfo> CODEC = RecordCodecBuilder.mapCodec(in ->{
        return in.group(ItemStack.CODEC.fieldOf("drawer").forGetter(recipe ->{
            return recipe.drawer;
        }),ItemStack.CODEC.fieldOf("infinity_item").forGetter(recipe ->{
            return recipe.infinityItem;
        })).apply(in,CreativeDrawerInfo::new);
    });

    public ItemStack drawer;
    public ItemStack infinityItem;
    public CreativeDrawerInfo(ItemStack drawer, ItemStack infinityItem){
        this.drawer = drawer;
        this.infinityItem = infinityItem;
    }
    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return CDPRecipe.CREATIVE_DRAWER_INFO_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return CDPRecipe.CREATIVE_DRAWER_INFO_TYPE.get();
    }
}
