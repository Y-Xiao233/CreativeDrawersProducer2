package net.yxiao233.cdp2.common.integration.kubejs.component;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.component.ListRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.recipe.ChanceIngredient;

public record ChanceIngredientComponent(RecipeComponentType<?> type) implements RecipeComponent<ChanceIngredient> {
    public static final RecipeComponentType<ChanceIngredient> SINGLE = RecipeComponentType.unit(CreativeDrawersProducer2.makeId("chance_ingredient"), ChanceIngredientComponent::new);
    public static final ListRecipeComponent<ChanceIngredient> LIST = ListRecipeComponent.create(new ChanceIngredientComponent(SINGLE),true,true);
    @Override
    public RecipeComponentType<?> type() {
        return type;
    }

    @Override
    public Codec<ChanceIngredient> codec() {
        return ChanceIngredient.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TypeInfo.of(ChanceIngredient.class);
    }
}
