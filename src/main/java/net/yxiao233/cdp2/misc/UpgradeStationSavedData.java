package net.yxiao233.cdp2.misc;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public class UpgradeStationSavedData extends SavedData implements INBTSerializable<CompoundTag> {
    private int value;
    public UpgradeStationSavedData(){
        this.value = 0;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int value){
        this.value = value;
        setDirty();
    }

    public void addValue(int delta){
        this.value += delta;
        setDirty();
    }
    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        compoundTag.putInt("upgrade_station_point",value);
        return compoundTag;
    }

    public static UpgradeStationSavedData load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider){
        UpgradeStationSavedData data = new UpgradeStationSavedData();
        data.value = tag.getInt("upgrade_station_point");
        return data;
    }

    public static UpgradeStationSavedData get(ServerLevel level){
        return level.getDataStorage().computeIfAbsent(new Factory<>(UpgradeStationSavedData::new,UpgradeStationSavedData::load), "upgrade_station");
    }

    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        return save(new CompoundTag(),provider);
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        load(tag,provider);
    }
}
