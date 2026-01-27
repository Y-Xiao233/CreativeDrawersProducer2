package net.yxiao233.cdp2.integration.botany_pot;

import net.darkhax.botanypots.common.impl.block.PotType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CDPBotanyPotBlockItem extends BlockItem {
    private final PotType type;
    private final CDPPotTier potTier;
    public CDPBotanyPotBlockItem(PotType type, CDPPotTier potTier, Block block, Properties properties) {
        super(block, properties);
        this.type = type;
        this.potTier = potTier;
    }


    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable("potTier.cdp2." + potTier.getName()).append(Component.translatable("potType.cdp2."+ type.name().toLowerCase()));
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable("potTier.cdp2." + potTier.getName()).append(Component.translatable("potType.cdp2."+ type.name().toLowerCase()));
    }

    public CDPPotTier getPotTier() {
        return potTier;
    }

    public PotType getType() {
        return type;
    }
}
