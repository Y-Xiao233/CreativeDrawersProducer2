package net.yxiao233.cdp2.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.yxiao233.cdp2.common.registry.CDPBlock;
import org.jetbrains.annotations.NotNull;

public class VoidCraftingTableBlock extends CraftingTableBlock {
    private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");
    public VoidCraftingTableBlock() {
        super(Properties.ofFullCopy(Blocks.AMETHYST_BLOCK));
    }

    @Override
    protected MenuProvider getMenuProvider(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        ContainerLevelAccess access = ContainerLevelAccess.create(level, pos);
        return new SimpleMenuProvider((id, inventory, player) -> {
            return new CraftingMenu(id, inventory, access){
                @Override
                public boolean stillValid(@NotNull Player player) {
                    return stillValid(access,player, CDPBlock.VOID_CRAFTING_TABLE.asBlock());
                }
            };
        }, CONTAINER_TITLE);
    }
}
