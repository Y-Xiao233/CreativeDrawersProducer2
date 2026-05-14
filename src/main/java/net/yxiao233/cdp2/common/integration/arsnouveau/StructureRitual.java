package net.yxiao233.cdp2.common.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.ritual.AbstractRitual;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.yxiao233.cdp2.CreativeDrawersProducer2;
import net.yxiao233.cdp2.api.structure.StructureBuilder;

public class StructureRitual extends AbstractRitual{
    public final String name;
    public final String nbtPath;
    public StructureRitual(String registryName, String nbtPath){
        super();
        this.name = registryName;
        this.nbtPath = nbtPath;
    }
    @Override
    protected void tick() {
        Level level = getWorld();
        BlockPos pos = getPos();
        if(level instanceof ServerLevel serverLevel && pos != null){
            new StructureBuilder(nbtPath).buildStructure(serverLevel, pos);
            setFinished();
        }
    }

    @Override
    public ResourceLocation getRegistryName() {
        return CreativeDrawersProducer2.makeId(this.name);
    }
}
