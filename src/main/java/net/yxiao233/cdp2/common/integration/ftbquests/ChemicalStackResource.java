package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ui.resource.SelectableResource;
import dev.ftb.mods.ftblibrary.icon.Icon;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.Component;

public record ChemicalStackResource(ChemicalStack resource) implements SelectableResource<ChemicalStack> {
    static SelectableResource<ChemicalStack> chemical(ChemicalStack stack) {
        return new ChemicalStackResource(stack);
    }
    @Override
    public long getCount() {
        return this.resource.getAmount();
    }

    @Override
    public boolean isEmpty() {
        return this.resource.isEmpty();
    }

    @Override
    public void setCount(int i) {
        this.resource.setAmount(i);
    }

    @Override
    public Component getName() {
        return this.resource.getTextComponent();
    }

    @Override
    public Icon getIcon() {
        return new ChemicalIcon(this.resource);
    }

    @Override
    public SelectableResource<ChemicalStack> copyWithCount(long l) {
        return new ChemicalStackResource(this.resource.copyWithAmount(l));
    }
}
