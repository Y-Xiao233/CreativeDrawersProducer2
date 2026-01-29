package net.yxiao233.cdp2.integration.botanypot;

import net.darkhax.botanypots.common.api.context.BotanyPotContext;
import net.darkhax.botanypots.common.api.data.recipes.crop.Crop;
import net.darkhax.botanypots.common.api.data.recipes.soil.Soil;
import net.darkhax.botanypots.common.impl.block.BotanyPotBlock;
import net.darkhax.botanypots.common.impl.block.PotType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CDPBotanyPotEntityBlock extends BotanyPotBlock {
    private final CDPPotTier potTier;
    private final ResourceLocation entityTypeLocation;
    private final PotType potType;
    public CDPBotanyPotEntityBlock(ResourceLocation entityTypeLocation, PotType type, CDPPotTier potTier) {
        super(MapColor.COLOR_ORANGE, type);
        this.potTier = potTier;
        this.potType = type;
        this.entityTypeLocation = entityTypeLocation;
    }

    @Override
    public float getGrowthModifier(BotanyPotContext context, Level level, Crop crop, @Nullable Soil soil) {
        return this.potTier.getSpeedMultiplier();
    }

    @Override
    public float getYieldModifier(BotanyPotContext context, Level level, Crop crop, @Nullable Soil soil) {
        return this.potTier.getOutputMultiplier();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) {
            components.add(Component.translatable("tooltip.botanypotstiers.tiered_botany_pot.multiplier", new Object[]{this.potTier.getOutputMultiplier()}).withStyle(ChatFormatting.AQUA));
            components.add(Component.translatable("tooltip.botanypotstiers.tiered_botany_pot.speed", new Object[]{this.potTier.getSpeedMultiplier()}).withStyle(ChatFormatting.AQUA));
        } else {
            components.add(Component.translatable("tooltip.botanypotstiers.tiered_botany_pot.pressShiftForMore").withStyle(ChatFormatting.YELLOW));
        }
    }


    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, (BlockEntityType<T>) CDPBlock.POTS_MAP.get(entityTypeLocation).asBlockEntityType(), (level1, pos, state1, pot) -> {
            CDPBotanyPotBlockEntity.tickPot(level1, pos, state1, (CDPBotanyPotBlockEntity) pot);
        });
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CDPBotanyPotBlockEntity(pos,state,this.potTier,entityTypeLocation);
    }

    public CDPPotTier getPotTier() {
        return potTier;
    }

    public PotType getPotType() {
        return potType;
    }
}
