package net.yxiao233.cdp2.mixin.minecraft;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.yxiao233.cdp2.api.minecraft.ICDPStructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

/**
 * Lets the burning arena drop the useless netherrack layer that sits on top of four of its templates (the two lower
 * halves, {@code burning_arena5} to {@code burning_arena8}, are 34 blocks tall and their highest layer is pure
 * netherrack).
 *
 * <p>Only blocks of the highest layer are considered and only when they are netherrack, so the templates whose top
 * layer is the actual building (the upper halves {@code burning_arena1} to {@code burning_arena4}) are left alone.</p>
 */
@Mixin(StructureTemplate.class)
public abstract class StructureTemplateMixin implements ICDPStructureTemplate {
    @Shadow
    @Final
    private List<StructureTemplate.Palette> palettes;

    @Override
    @Unique
    public void cdp2$removeTopNetherrackLayer(){
        for(StructureTemplate.Palette palette : this.palettes){
            List<StructureTemplate.StructureBlockInfo> blocks = palette.blocks();
            if(blocks.isEmpty()){
                continue;
            }
            int highest = Integer.MIN_VALUE;
            for(StructureTemplate.StructureBlockInfo info : blocks){
                highest = Math.max(highest,info.pos().getY());
            }
            final int topY = highest;
            blocks.removeIf(info -> info.pos().getY() == topY && info.state().is(Blocks.NETHERRACK));
        }
    }
}
