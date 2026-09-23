package net.yxiao233.cdp2.common.integration.jei.converter;

import appeng.client.gui.style.Blitter;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKey;
import net.yxiao233.cdp2.common.integration.ae2.key.ForbiddenEssenceKeyRenderHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CDPIngredientTypes {
    public static final IIngredientType<ForbiddenEssenceStack> ESSENCE_TYPE = () -> ForbiddenEssenceStack.class;
    public static class EssenceStackHelper implements IIngredientHelper<ForbiddenEssenceStack> {

        @Override
        public @NotNull IIngredientType<ForbiddenEssenceStack> getIngredientType() {
            return ESSENCE_TYPE;
        }

        @Override
        public @NotNull String getDisplayName(ForbiddenEssenceStack ingredient) {
            return ingredient.type().getSerializedName();
        }

        @Override
        @SuppressWarnings("removal")
        public @NotNull String getUniqueId(@NotNull ForbiddenEssenceStack ingredient, @NotNull UidContext context) {
            return "forbidden_essence:" + ingredient.type().getSerializedName();
        }

        @Override
        public @NotNull ResourceLocation getResourceLocation(@NotNull ForbiddenEssenceStack ingredient) {
            return CreativeDrawersProducer2.makeId("forbidden_" + ingredient.type().getSerializedName() + "_essence");
        }

        @Override
        public @NotNull ForbiddenEssenceStack copyIngredient(ForbiddenEssenceStack ingredient) {
            return new ForbiddenEssenceStack(ingredient.type(),ingredient.amount());
        }

        @Override
        public @NotNull String getErrorInfo(@Nullable ForbiddenEssenceStack ingredient) {
            return null;
        }
    }

    public static class EssenceStackRenderer implements IIngredientRenderer<ForbiddenEssenceStack> {

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, ForbiddenEssenceStack ingredient) {
            Blitter.sprite(ForbiddenEssenceKeyRenderHandler.spriteFor(ForbiddenEssenceKey.of(ingredient.type()))).dest(0,0, 16, 16).blit(guiGraphics);
        }

        @Override
        public @NotNull List<Component> getTooltip(ForbiddenEssenceStack ingredient, @NotNull TooltipFlag tooltipFlag) {
            return List.of(ForbiddenEssenceKey.of(ingredient.type()).getDisplayName());
        }
    }
}
