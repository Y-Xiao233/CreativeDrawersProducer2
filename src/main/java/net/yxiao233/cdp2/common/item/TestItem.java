package net.yxiao233.cdp2.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.structure.StructureBuilder;
import org.jetbrains.annotations.NotNull;

public class TestItem extends Item {
    public TestItem() {
        super(new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if(level instanceof ServerLevel serverLevel){
            BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
            new StructureBuilder("test").buildStructure(serverLevel, placePos);
        }
        return InteractionResult.SUCCESS;
    }
}
