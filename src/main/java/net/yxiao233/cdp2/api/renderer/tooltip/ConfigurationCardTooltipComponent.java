package net.yxiao233.cdp2.api.renderer.tooltip;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.yxiao233.cdp2.common.integration.botanypot.item.ConfigurationCardItem;

@OnlyIn(Dist.CLIENT)
public record ConfigurationCardTooltipComponent(ItemStack soil, ItemStack seed) implements TooltipComponent {
    public ClientTooltipComponent getClientTooltipComponent() {
        return new ConfigurationCardTooltipRenderer(this);
    }
}
