package net.yxiao233.cdp2.common.integration.botanypot.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;
import net.yxiao233.cdp2.api.renderer.tooltip.ConfigurationCardTooltipComponent;
import net.yxiao233.cdp2.common.integration.botanypot.PotConfigurationAction;
import net.yxiao233.cdp2.common.integration.botanypot.datacomponent.PotInfo;
import net.yxiao233.cdp2.common.registry.CDPDataComponentTypes;
import net.yxiao233.cdp2.util.EntityUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ConfigurationCardItem extends Item {
    public ConfigurationCardItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        ItemStack card = context.getItemInHand();
        Player player = context.getPlayer();
        if(player != null && player.isShiftKeyDown()){
            PotConfigurationAction.create(card,blockEntity).action(player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        HitResult hitResult = EntityUtil.rayTraceSimple(level, player, 13, 0);
        ItemStack card = player.getItemInHand(usedHand);
        if(hitResult.getType() == HitResult.Type.BLOCK){
            return InteractionResultHolder.pass(card);
        }
        if(card.getItem() instanceof ConfigurationCardItem && player.isShiftKeyDown()){
            if(card.has(CDPDataComponentTypes.POT_INFO)){
                card.remove(CDPDataComponentTypes.POT_INFO);
            }
            player.displayClientMessage(PotConfigurationAction.reset.get(0),true);
            return InteractionResultHolder.success(card);
        }
        return InteractionResultHolder.pass(card);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        if(stack.has(CDPDataComponentTypes.POT_INFO)){
            PotInfo potInfo = stack.get(CDPDataComponentTypes.POT_INFO);
            if(potInfo != null && !potInfo.soil().isEmpty() && !potInfo.seed().isEmpty()){
                return Optional.of(new ConfigurationCardTooltipComponent(potInfo.soil(),potInfo.seed()));
            }
        }
        return super.getTooltipImage(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.cdp2.configuration_card.tip0").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("tooltip.cdp2.configuration_card.tip1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if(stack.has(CDPDataComponentTypes.POT_INFO)){
            PotInfo potInfo = stack.get(CDPDataComponentTypes.POT_INFO);
            return potInfo != null && !potInfo.seed().isEmpty() && !potInfo.soil().isEmpty();
        }
        return false;
    }
}
