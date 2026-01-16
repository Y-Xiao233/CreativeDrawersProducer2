package net.yxiao233.cdp2.common.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.client.renderer.RandomCreativeDrawerItemRenderer;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class RandomCreativeDrawerItem extends Item {
    public RandomCreativeDrawerItem(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("removal")
    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final RandomCreativeDrawerItemRenderer renderer = new RandomCreativeDrawerItemRenderer(CDPBlock.CREATIVE_DRAWERS_MAP);
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        if(!level.isClientSide()){
            var drawersMap = CDPBlock.CREATIVE_DRAWERS_MAP;
            ItemStack handItem = player.getItemInHand(usedHand);
            if(drawersMap.isEmpty()){
                return InteractionResultHolder.pass(handItem);
            }
            int index = level.getRandom().nextInt(drawersMap.size());
            ItemHandlerHelper.giveItemToPlayer(player,drawersMap.values().stream().toList().get(index).asItem().getDefaultInstance());
            player.setItemInHand(usedHand,handItem.copyWithCount(handItem.getCount() - 1));
            return InteractionResultHolder.consume(handItem);
        }
        return InteractionResultHolder.pass(player.getItemInHand(usedHand));
    }
}
