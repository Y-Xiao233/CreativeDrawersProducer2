package net.yxiao233.cdp2.common.integration.jei.converter;

import appeng.api.stacks.GenericStack;
import mezz.jei.api.ingredients.IIngredientType;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tamaized.ae2jeiintegration.api.integrations.jei.IngredientConverter;

public record ForbiddenEssenceConverter(IIngredientType<ForbiddenEssenceStack> type) implements IngredientConverter<ForbiddenEssenceStack> {
    @Override
    public @NotNull IIngredientType<ForbiddenEssenceStack> getIngredientType() {
        return type;
    }

    @Override
    public @Nullable ForbiddenEssenceStack getIngredientFromStack(GenericStack stack) {
        if (stack.what() instanceof ForbiddenEssenceKey key) {
            return new ForbiddenEssenceStack(key.type(), (int) Math.min(Integer.MAX_VALUE, stack.amount()));
        }
        return null;
    }

    @Override
    public @NotNull GenericStack getStackFromIngredient(ForbiddenEssenceStack stack) {
        return new GenericStack(ForbiddenEssenceKey.of(stack.type()),stack.amount());
    }
}
