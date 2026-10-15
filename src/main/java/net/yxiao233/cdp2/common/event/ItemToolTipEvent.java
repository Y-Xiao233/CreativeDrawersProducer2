package net.yxiao233.cdp2.common.event;

import com.buuz135.industrial.item.MobImprisonmentToolItem;
import dev.shadowsoffire.apothic_spawners.ASObjects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.common.integration.kubejs.event.BlockModifyEvent;

@SuppressWarnings({"removal","unused"})
@EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ItemToolTipEvent {
    @SubscribeEvent
    public static void addTip(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        Level level = event.getContext().level();
        mobDuplicatorEnabledInformation(event,stack,level);
        spawnEggDisabledInformation(event,stack);
    }
    public static void mobDuplicatorEnabledInformation(ItemTooltipEvent event, ItemStack stack, Level level){
        if(stack.getItem() instanceof MobImprisonmentToolItem toolItem){
            Entity entityFromStack = toolItem.getEntityFromStack(stack, level, true, true);
            if(entityFromStack != null){
                String id = entityFromStack.getEncodeId();
                if(BlockModifyEvent.WHITE_LIST.contains(id)){
                    event.getToolTip().add(Component.translatable("tooltip.cdp2.mob_imprisonment_tool.enable").withStyle(ChatFormatting.GREEN));
                }else{
                    event.getToolTip().add(Component.translatable("tooltip.cdp2.mob_imprisonment_tool.disable").withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    public static void spawnEggDisabledInformation(ItemTooltipEvent event, ItemStack stack){
        if(stack.getItem() instanceof SpawnEggItem spawnEggItem){
            EntityType<?> type = spawnEggItem.getType(stack);
            if(type.is(ASObjects.BLACKLISTED_FROM_SPAWNERS)){
                event.getToolTip().add(Component.translatable("tooltip.cdp2.blacklisted_from_spawners").withStyle(ChatFormatting.RED));
            }
        }
    }
}
