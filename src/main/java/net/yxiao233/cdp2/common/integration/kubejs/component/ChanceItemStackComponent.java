package net.yxiao233.cdp2.common.integration.kubejs.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.ListRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.recipe.ChanceItemStack;

public record ChanceItemStackComponent(RecipeComponentType<?> type) implements RecipeComponent<ChanceItemStack> {
    public static final RecipeComponentType<ChanceItemStack> SINGLE = RecipeComponentType.unit(CreativeDrawersProducer2.makeId("chance_item_stack"), ChanceItemStackComponent::new);
    public static final ListRecipeComponent<ChanceItemStack> LIST = ListRecipeComponent.create(new ChanceItemStackComponent(SINGLE),true,true);
    @Override
    public RecipeComponentType<?> type() {
        return type;
    }

    @Override
    public Codec<ChanceItemStack> codec() {
        return ChanceItemStack.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(ChanceItemStack.class);
    }
}

