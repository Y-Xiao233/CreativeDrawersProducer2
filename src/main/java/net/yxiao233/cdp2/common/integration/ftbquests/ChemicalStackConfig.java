package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ResourceConfigValue;
import dev.ftb.mods.ftblibrary.config.ui.resource.SelectableResource;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import mekanism.api.chemical.ChemicalStack;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

public class ChemicalStackConfig extends ResourceConfigValue<ChemicalStack> {
    private final boolean allowEmpty;
    private final boolean isFixedSize;
    private final long fixedSize;
    public ChemicalStackConfig(boolean single, boolean empty) {
        this.isFixedSize = single && !empty;
        this.fixedSize = 0L;
        this.allowEmpty = empty;
        this.defaultValue = ChemicalStack.EMPTY;
        this.value = ChemicalStack.EMPTY;
    }
    public ChemicalStackConfig(long fixedSize) {
        Validate.isTrue(fixedSize >= 1L);
        this.isFixedSize = true;
        this.fixedSize = fixedSize;
        this.allowEmpty = false;
        this.defaultValue = ChemicalStack.EMPTY;
        this.value = ChemicalStack.EMPTY;
    }

    @Override
    public ChemicalStack copy(ChemicalStack value) {
        return value.isEmpty() ? ChemicalStack.EMPTY : value.copy();
    }

    @Override
    public Component getStringForGUI(@Nullable ChemicalStack v) {
        if (v != null && !v.isEmpty()) {
            return (Component)(v.getAmount() <= 1 ? v.getTextComponent() : Component.literal(v.getAmount() + "x ").append(v.getTextComponent()));
        } else {
            return Component.translatable("gui.none");
        }
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            (new SelectChemicalStackScreen(this, callback)).openGui();
        }
    }

    @Override
    public ChemicalStack getValue() {
        ChemicalStack val = super.getValue();
        return val.isEmpty() ? ChemicalStack.EMPTY : val;
    }

    @Override
    public boolean allowEmptyResource() {
        return this.allowEmpty;
    }

    @Override
    public OptionalLong fixedResourceSize() {
        return this.isFixedSize ? OptionalLong.of(this.fixedSize) : OptionalLong.empty();
    }

    @Override
    public boolean isEmpty() {
        return this.getValue().isEmpty();
    }

    @Override
    public SelectableResource<ChemicalStack> getResource() {
        return ChemicalStackResource.chemical(this.getValue());
    }

    @Override
    public boolean setResource(SelectableResource<ChemicalStack> selectable) {
        return this.setCurrentValue(selectable.resource());
    }
}
