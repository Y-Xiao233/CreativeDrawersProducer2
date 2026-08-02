package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ui.resource.ResourceSearchMode;
import dev.ftb.mods.ftblibrary.config.ui.resource.SelectableResource;
import dev.ftb.mods.ftblibrary.icon.Icons;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ChemicalSearchMode {
    ResourceSearchMode<ChemicalStack> ALL_CHEMICALS = new ResourceSearchMode.SearchMode<>(Component.literal("所有气体"), Icons.COMPASS) {
        private List<SelectableResource<ChemicalStack>> allChemicalsCache = null;

        @Override
        public Collection<? extends SelectableResource<ChemicalStack>> getAllResources() {
            if (this.allChemicalsCache == null) {
                List<SelectableResource<ChemicalStack>> chemicalStacks = new ArrayList<>();
                MekanismAPI.CHEMICAL_REGISTRY.forEach(chemical -> {
                    chemicalStacks.add(ChemicalStackResource.chemical(new ChemicalStack(Holder.direct(chemical), 1000)));
                });
                this.allChemicalsCache = List.copyOf(chemicalStacks);
            }

            return this.allChemicalsCache;
        }
    };
}
