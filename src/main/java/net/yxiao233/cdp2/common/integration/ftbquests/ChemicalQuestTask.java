package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.Button;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.attachments.containers.chemical.AttachedChemicals;
import mekanism.common.item.ItemGaugeDropper;
import mekanism.common.item.block.ItemBlockChemicalTank;
import mekanism.common.registries.MekanismDataComponents;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.cdp2.util.JeiUtil;

import java.util.concurrent.atomic.AtomicLong;

public class ChemicalQuestTask extends Task {
    private ChemicalStack stack;
    public ChemicalQuestTask(long id, Quest quest) {
        super(id, quest);
        this.stack = ChemicalStack.EMPTY;
    }

    @Override
    public long getMaxProgress() {
        return this.stack.getAmount();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        config.add("chemical",new ChemicalStackConfig(true,false),this.stack,(v) ->{
            this.stack = v;
        },ChemicalStack.EMPTY);
    }

    @Override
    public TaskType getType() {
        return CDPTaskTypes.CHEMICAL;
    }

    @Override
    public void writeData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.writeData(nbt, provider);
        nbt.put("chemical", this.stack.saveOptional(provider));
    }


    @Override
    public void readData(CompoundTag nbt, HolderLookup.Provider provider) {
        super.readData(nbt,provider);
        this.stack = ChemicalStack.parseOptional(provider,nbt.getCompound("chemical"));
    }
    @Override
    public void writeNetData(RegistryFriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        ChemicalStack.OPTIONAL_STREAM_CODEC.encode(buffer,this.stack);
    }

    @Override
    public void readNetData(RegistryFriendlyByteBuf buffer) {
        super.readNetData(buffer);
        this.stack = ChemicalStack.OPTIONAL_STREAM_CODEC.decode(buffer);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Component getAltTitle() {
        Component name = Component.translatable(this.stack.getTranslationKey());
        long amount = this.stack.getAmount();
        return amount == 1 ? name : Component.literal(amount + "x ").append(name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Icon getAltIcon() {
        return new ChemicalIcon(this.stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onButtonClicked(Button button, boolean canClick) {
        button.playClickSound();
        if (!stack.isEmpty()) {
            JeiUtil.openRecipeGui(RecipeIngredientRole.OUTPUT,stack);
        }
    }

    @Override
    public int autoSubmitOnPlayerTick() {
        return 5;
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
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
        }else if(amount.get() > teamData.getProgress(this)){
            teamData.setProgress(this,amount.get());
        }
    }

//    @SuppressWarnings("removal")
//    @EventBusSubscriber(modid = CreativeDrawersProducer2.MODID, bus = EventBusSubscriber.Bus.GAME)
//    public static class Handler {
//        @SubscribeEvent
//        public static void onPlayerTick(PlayerTickEvent.Post event) {
//            if (!(event.getEntity() instanceof ServerPlayer player)) return;
//            if (player.level().getGameTime() % 5 != 0) return;
//            ServerQuestFile file = ServerQuestFile.INSTANCE;
//            if (file == null) return;
//            file.getTeamData(player).ifPresent(teamData -> {
//                for (Task task : file.collect(ChemicalQuestTask.class, t -> true)) {
//                    if (!teamData.isCompleted(task) && teamData.canStartTasks(task.getQuest())) {
//                        task.submitTask(teamData, player, ItemStack.EMPTY);
//                    }
//                }
//            });
//        }
//    }
}
