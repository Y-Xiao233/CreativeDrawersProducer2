package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ResourceConfigValue;
import dev.ftb.mods.ftblibrary.config.ui.resource.*;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.util.SearchTerms;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Objects;

public class SelectChemicalStackScreen extends ResourceSelectorScreen<ChemicalStack> {
    public static final SearchModeIndex<ResourceSearchMode<ChemicalStack>> KNOWN_MODES = Util.make(new SearchModeIndex<>(), (index) -> {
        index.appendMode(ChemicalSearchMode.ALL_CHEMICALS);
        index.appendMode(ChemicalSearchMode.INVENTORY);
    });
    public SelectChemicalStackScreen(ResourceConfigValue<ChemicalStack> config, ConfigCallback callback) {
        super(config, callback);
    }

    @Override
    protected SearchModeIndex<ResourceSearchMode<ChemicalStack>> getSearchModeIndex() {
        return KNOWN_MODES;
    }

    @Override
    protected ResourceSelectorScreen<ChemicalStack>.ResourceButton makeResourceButton(Panel panel, SelectableResource<ChemicalStack> resource) {
        return new SelectChemicalStackScreen.ChemicalStackButton(panel, Objects.requireNonNullElse(resource, ChemicalStackResource.chemical(ChemicalStack.EMPTY)));
    }

    public class ChemicalStackButton extends ResourceSelectorScreen<ChemicalStack>.ResourceButton{
        private ChemicalStackButton(Panel panel, SelectableResource<ChemicalStack> resource) {
            super(panel, resource);
        }
        @Override
        @SuppressWarnings("deprecation")
        public boolean shouldAdd(SearchTerms searchTerms) {
            return searchTerms.match(RegistrarManager.getId((this.getResource()).getChemical(), MekanismAPI.CHEMICAL_REGISTRY), (this.getResource()).getTextComponent().getString(), (id) -> true);
        }

        public void addMouseOverText(TooltipList list) {
            if (!(this.getResource()).isEmpty()) {
                List<Component> flatList = this.getResource().getTextComponent().toFlatList();
                Objects.requireNonNull(list);
                flatList.forEach(list::add);
                list.add(Component.literal(MekanismAPI.CHEMICAL_REGISTRY.getKey(this.getResource().getChemical()).getNamespace()).withStyle(new ChatFormatting[]{ChatFormatting.BLUE, ChatFormatting.ITALIC}));
            }
        }
    }
}
