package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.attachments.containers.chemical.AttachedChemicals;
import mekanism.common.item.ItemGaugeDropper;
import mekanism.common.item.block.ItemBlockChemicalTank;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.concurrent.atomic.AtomicLong;

public class ChemicalQuestTask extends Task {
    private ChemicalStack stack;
    private long amount;
    public ChemicalQuestTask(long id, Quest quest) {
        super(id, quest);
        this.stack = ChemicalStack.EMPTY;
        this.amount = 1L;
    }

    @Override
    public long getMaxProgress() {
        return this.amount;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("chemical",new ChemicalStackConfig(true,false),this.stack,(v) ->{
            this.stack = v;
        },ChemicalStack.EMPTY);
        config.addLong("amount", this.amount, (v) ->{
            this.amount = v;
        }, 1L, 1L, Long.MAX_VALUE);
    }

    @Override
    public TaskType getType() {
        return CDPTaskTypes.CHEMICAL;
    }

    @Override
    public void writeData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.writeData(nbt, provider);
        nbt.put("chemical", this.stack.saveOptional(provider));
        nbt.putLong("amount",this.amount);
    }


    @Override
    public void readData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.readData(nbt,provider);
        this.stack = ChemicalStack.parseOptional(provider,nbt.getCompound("chemical"));
        this.amount = nbt.getLong("amount");
    }
    @Override
    public void writeNetData(RegistryFriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        ChemicalStack.OPTIONAL_STREAM_CODEC.encode(buffer,this.stack);
        buffer.writeLong(this.amount);
    }

    @Override
    public void readNetData(RegistryFriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stack = ChemicalStack.OPTIONAL_STREAM_CODEC.decode(buffer);
        this.amount = buffer.readLong();
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 5;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getAltTitle() {
        Component name = Component.translatable(this.stack.getTranslationKey());
        return amount == 1 ? name : Component.literal(amount + "x ").append(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Icon getAltIcon() {
        return new ChemicalIcon(this.stack);
    }


    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        //TODO 未知原因: 只能在新建好任务后大/小退游戏后才能正常检测
        AtomicLong amount = new AtomicLong();
        for(int i = 0; i < player.getInventory().items.size(); ++i) {
            ItemStack stack = player.getInventory().items.get(i);
            if(stack.getItem() instanceof ItemBlockChemicalTank || stack.getItem() instanceof ItemGaugeDropper){
                if(stack.has(MekanismDataComponents.ATTACHED_CHEMICALS)){
                    AttachedChemicals chemicalStacks = stack.get(MekanismDataComponents.ATTACHED_CHEMICALS);
                    if(chemicalStacks != null){
                        ChemicalStack chemicalStack = chemicalStacks.get(0);
                        if(chemicalStack.is(this.stack.getChemical())){
                            amount.addAndGet(chemicalStack.getAmount());
                        }
                    }
                }
            }
        }
        if(amount.get() >= this.getMaxProgress()){
            teamData.setProgress(this,this.getMaxProgress());
        }else{
            teamData.resetProgress(this);
        }
    }
}
