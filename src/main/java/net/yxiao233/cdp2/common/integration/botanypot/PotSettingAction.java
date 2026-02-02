package net.yxiao233.cdp2.common.integration.botanypot;

import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.yxiao233.cdp2.common.integration.botanypot.datacomponent.PotInfo;
import net.yxiao233.cdp2.common.integration.botanypot.item.ConfigurationCardItem;
import net.yxiao233.cdp2.common.registry.CDPDataComponentTypes;

import java.util.HashMap;

public class PotSettingAction {
    public static final PotSettingAction EMPTY = new PotSettingAction(null,null);
    public static HashMap<Integer, Component> applyOrSave;
    public static HashMap<Integer, Component> reset;
    private final ItemStack card;
    private final BotanyPotBlockEntity entity;
    private PotSettingAction(ItemStack stack, BotanyPotBlockEntity entity){
        this.card = stack;
        this.entity = entity;
    }

    public static PotSettingAction create(ItemStack card, BlockEntity blockEntity){
        if(card.getItem() instanceof ConfigurationCardItem && blockEntity instanceof BotanyPotBlockEntity potBlockEntity){
            return new PotSettingAction(card,potBlockEntity);
        }
        return EMPTY;
    }

    public void action(Player player){
        if(this == EMPTY || this.entity == null || this.card == null || this.card.isEmpty()){
            return;
        }
        boolean has = card.has(CDPDataComponentTypes.POT_INFO);
        int messageValue;
        if(has){
            messageValue = applySetting(player);
        }else{
            messageValue = getSetting();
        }
        Component message = PotSettingAction.applyOrSave.get(messageValue);
        player.displayClientMessage(message,true);
    }

    private int applySetting(Player player){
        PotInfo potInfo = card.get(CDPDataComponentTypes.POT_INFO);
        if(potInfo == null){
            return -1;
        }
        ItemStack potSeed = entity.getSeedItem().copy();
        ItemStack potSoil = entity.getSoilItem().copy();
        ItemStack seed = potInfo.seed().copy();
        ItemStack soil = potInfo.soil().copy();
        if(potSeed.isEmpty() || !ItemStack.isSameItemSameComponents(potSeed,seed)){
            int seedSlot = player.getInventory().findSlotMatchingItem(seed);
            if (seedSlot != -1) {
                entity.setSeed(seed);
                player.getInventory().getItem(seedSlot).shrink(1);
            }else{
                return 1;
            }
            if(!potSeed.isEmpty()){
                ItemHandlerHelper.giveItemToPlayer(player,potSeed);
            }
        }
        if(potSoil.isEmpty() || !ItemStack.isSameItemSameComponents(potSoil,soil)){
            int soilSlot = player.getInventory().findSlotMatchingItem(soil);
            if (soilSlot != -1) {
                entity.setSoilItem(soil);
                player.getInventory().getItem(soilSlot).shrink(1);
            }else{
                return 2;
            }
            if(!potSoil.isEmpty()){
                ItemHandlerHelper.giveItemToPlayer(player,potSoil);
            }
        }
        return 0;
    }

    private int getSetting(){
        ItemStack potSeed = entity.getSeedItem().copy();
        ItemStack potSoil = entity.getSoilItem().copy();
        if(potSeed.isEmpty()){
            return 11;
        }
        if(potSoil.isEmpty()){
            return 12;
        }

        card.set(CDPDataComponentTypes.POT_INFO,PotInfo.of(potSoil,potSeed));
        return 10;
    }


    static{
        applyOrSave = new HashMap<>();
        applyOrSave.put(-1,Component.translatable("message.cdp2.configuration_card.error").withStyle(ChatFormatting.RED));
        applyOrSave.put(0,Component.translatable("message.cdp2.configuration_card.success").withStyle(ChatFormatting.GREEN));
        applyOrSave.put(1,Component.translatable("message.cdp2.configuration_card.miss_seed").withStyle(ChatFormatting.YELLOW));
        applyOrSave.put(2,Component.translatable("message.cdp2.configuration_card.miss_soil").withStyle(ChatFormatting.YELLOW));
        applyOrSave.put(10,Component.translatable("message.cdp2.configuration_card.save").withStyle(ChatFormatting.GREEN));
        applyOrSave.put(11,Component.translatable("message.cdp2.configuration_card.null_seed").withStyle(ChatFormatting.RED));
        applyOrSave.put(12,Component.translatable("message.cdp2.configuration_card.null_soil").withStyle(ChatFormatting.RED));

        reset = new HashMap<>();
        reset.put(0,Component.translatable("message.cdp2.configuration_card.reset").withStyle(ChatFormatting.GREEN));
    }
}
