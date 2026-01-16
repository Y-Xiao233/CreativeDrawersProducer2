package net.yxiao233.cdp2.api.renderer.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record ItemTooltipComponent(ItemStack stack) implements TooltipComponent {
    public ClientTooltipComponent getClientTooltipComponent() {
        return new ItemTooltipRenderer(this);
    }
}
