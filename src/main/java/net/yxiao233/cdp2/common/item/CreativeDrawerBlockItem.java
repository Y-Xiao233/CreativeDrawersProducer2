package net.yxiao233.cdp2.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.yxiao233.cdp2.api.renderer.tooltip.ItemTooltipComponent;
import net.yxiao233.cdp2.client.renderer.CreativeDrawerItemRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeDrawerBlockItem extends BlockItem {
    private final Supplier<ItemStack> infinityItem;
    public CreativeDrawerBlockItem(Block block, Supplier<ItemStack> infinityItem, Properties properties) {
        super(block, properties);
        this.infinityItem = infinityItem;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.cdp2.infinity").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable(infinityItem.get().getDescriptionId()).withStyle(ChatFormatting.AQUA).append(Component.translatable("block.cpd2.creative_drawer").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(infinityItem.get().getDescriptionId()).withStyle(ChatFormatting.AQUA).append(Component.translatable("block.cpd2.creative_drawer").withStyle(ChatFormatting.WHITE));
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        return Optional.of(new ItemTooltipComponent(infinityItem.get()));
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final CreativeDrawerItemRenderer renderer = new CreativeDrawerItemRenderer(infinityItem);
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
