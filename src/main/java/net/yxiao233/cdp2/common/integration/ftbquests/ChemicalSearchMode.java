package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ui.resource.ResourceSearchMode;
import dev.ftb.mods.ftblibrary.config.ui.resource.SelectableResource;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import mekanism.common.attachments.containers.chemical.AttachedChemicals;
import mekanism.common.item.ItemGaugeDropper;
import mekanism.common.item.block.ItemBlockChemicalTank;
import mekanism.common.registries.MekanismDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface ChemicalSearchMode {
    ResourceSearchMode<ChemicalStack> ALL_CHEMICALS = new ResourceSearchMode.SearchMode<>(Component.translatable("ftbquests.task.cdp2.chemical.all"), Icons.COMPASS) {
        private List<SelectableResource<ChemicalStack>> allChemicalsCache = null;

        @Override
        public Collection<? extends SelectableResource<ChemicalStack>> getAllResources() {
            if (this.allChemicalsCache == null) {
                List<SelectableResource<ChemicalStack>> chemicalStacks = new ArrayList<>();
                MekanismAPI.CHEMICAL_REGISTRY.forEach(chemical -> {
                    chemicalStacks.add(ChemicalStackResource.chemical(new ChemicalStack(Holder.direct(chemical), 1)));
                });
                this.allChemicalsCache = List.copyOf(chemicalStacks);
            }

            return this.allChemicalsCache;
        }
    };

    ResourceSearchMode<ChemicalStack> INVENTORY = new ResourceSearchMode.SearchMode<>(Component.translatable("ftbquests.task.cdp2.chemical.inventory"), ItemIcon.getItemIcon(Items.CHEST)) {
        @Override
        public Collection<? extends SelectableResource<ChemicalStack>> getAllResources() {
            Player player = Minecraft.getInstance().player;
            if (player == null) {
                return Collections.emptySet();
            } else {
                int invSize = player.getInventory().getContainerSize();
                List<SelectableResource<ChemicalStack>> stacks = new ArrayList<>(invSize);

                for(int i = 0; i < invSize; ++i) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if(stack.getItem() instanceof ItemBlockChemicalTank || stack.getItem() instanceof ItemGaugeDropper){
                        if(stack.has(MekanismDataComponents.ATTACHED_CHEMICALS)){
                            AttachedChemicals chemicalStacks = stack.get(MekanismDataComponents.ATTACHED_CHEMICALS);
                            if(chemicalStacks != null){
                                ChemicalStack chemicalStack = chemicalStacks.get(0);
                                stacks.add(ChemicalStackResource.chemical(chemicalStack));
                            }
                        }
                    }
                }

                return stacks;
            }
        }
    };
}
