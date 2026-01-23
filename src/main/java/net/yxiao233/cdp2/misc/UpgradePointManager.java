package net.yxiao233.cdp2.misc;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class UpgradePointManager implements INBTSerializable<CompoundTag> {
    private UpgradableTypes upgradableType;
    private UpgradePointManager(UpgradableTypes type){
        this.upgradableType = type;
    }
    public static UpgradePointManager of(UpgradableTypes template){
        UpgradableTypes upgradableType = UpgradableTypes.create();
        template.getType().forEach((k,p) ->{
            upgradableType.put(k,PointPair.of(p.getMax(), p.getPoint()));
        });
        return new UpgradePointManager(upgradableType);
    }

    public int getPoint(String key){
        return upgradableType.getPoint(key);
    }
    public int getMax(String key){
        return upgradableType.getType().getOrDefault(key,PointPair.of(0)).getMax();
    }

    public void updatePoint(String key, int point){
        upgradableType.set(key,point);
    }

    public void addPoint(String key, int added){
        upgradableType.add(key,added);
    }

    public int reset(String key){
        int point = getPoint(key);
        upgradableType.set(key,0);
        return point;
    }

    public int resetAll(){
        AtomicInteger point = new AtomicInteger(0);
        upgradableType.getType().forEach((key,pointPair) ->{
            point.getAndAdd(reset(key));
        });
        return point.get();
    }

    public HashMap<String, PointPair> getMap(){
        return upgradableType.getType();
    }
    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag tag = new CompoundTag();
        upgradableType.getType().forEach(((key,pointPair) ->{
            CompoundTag pointInformation = new CompoundTag();
            pointInformation.putInt("max",pointPair.getMax());
            pointInformation.putInt("point",pointPair.getPoint());
            tag.put(key,pointInformation);
        }));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag tag) {
        this.upgradableType = UpgradableTypes.create();

        tag.getAllKeys().forEach(key ->{
            CompoundTag type = tag.getCompound(key);
            int max = type.getInt("max");
            int point = type.getInt("point");
            this.upgradableType.put(key,PointPair.of(max,point));
        });
    }
}
