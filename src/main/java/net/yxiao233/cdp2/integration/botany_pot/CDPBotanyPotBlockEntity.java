package net.yxiao233.cdp2.integration.botany_pot;

import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.common.registry.CDPBlock;

public class CDPBotanyPotBlockEntity extends BotanyPotBlockEntity {
    private final CDPPotTier potTier;
    public CDPBotanyPotBlockEntity(BlockPos pos, BlockState state, CDPPotTier potTier, ResourceLocation entityTypeLocation) {
        super(CDPBlock.POTS_MAP.get(entityTypeLocation).asPotEntityType(),pos, state);
        this.potTier = potTier;
    }

    public static void tickPot(Level level, BlockPos pos, BlockState state, CDPBotanyPotBlockEntity pot) {
        BotanyPotBlockEntity.tickPot(level, pos, state, pot);
    }

    public CDPPotTier getPotTier() {
        return potTier;
    }
}
