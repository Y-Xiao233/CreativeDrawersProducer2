package net.yxiao233.cdp2.mixin.cataclysm;

import com.github.L_Ender.cataclysm.structures.CataclysmStructure;
import com.github.L_Ender.cataclysm.structures.Cursed_Pyramid_Structure;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.yxiao233.cdp2.worldgen.CDPRiverGuard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

/**
 * Cataclysm's cursed pyramid is a surface structure, so it must not be flooded by the unknown dimension's rivers
 * either. Its generation point comes from this shared base class, hence the injection here.
 */
@Mixin(CataclysmStructure.class)
public abstract class CataclysmStructureMixin {
    /** The cursed pyramid is about a hundred blocks across, so its whole footprint has to stay out of the rivers */
    @Unique
    private static final int cdp2$footprintRadius = 64;

    @ModifyReturnValue(
            method = "findGenerationPoint",
            at = @At("RETURN")
    )
    private Optional<Structure.GenerationStub> cdp2$notInRiver(Optional<Structure.GenerationStub> original, Structure.GenerationContext context){
        if(!(((Object)this) instanceof Cursed_Pyramid_Structure)){
            return original;
        }
        return CDPRiverGuard.rejectInRiver(original,context,cdp2$footprintRadius);
    }
}
