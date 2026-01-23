package net.yxiao233.cdp2.misc;

import java.util.HashMap;

public class UpgradableTypes {
    public static final UpgradableTypes EMPTY = create();
    public static final UpgradableTypes BASIC = create()
            .put("range",16);
    public static final UpgradableTypes MEKANISM = create()
            .put("energy",8)
            .put("speed",8)
            .put("filter",1)
            .put("muffling",1)
            .put("anchor",1)
            .put("chemical",8);
    private final HashMap<String, PointPair> pointMap = new HashMap<>();
    private UpgradableTypes(){
    }
    public static UpgradableTypes create(){
        return new UpgradableTypes();
    }

    public void put(String type, PointPair pointPair){
        pointMap.put(type,pointPair);
    }

    public UpgradableTypes put(String type, int max){
        pointMap.put(type,PointPair.of(max));
        return this;
    }
    public void add(String type, int added){
        pointMap.getOrDefault(type,PointPair.of(8).addPoint(added));
    }
    public void set(String type, int point){
        if(pointMap.containsKey(type)){
            PointPair pointPair = pointMap.get(type);
            pointMap.put(type,pointPair.setPoint(point));
        }
    }

    public int getPoint(String type){
        return pointMap.getOrDefault(type,PointPair.of(0)).getPoint();
    }

    public HashMap<String, PointPair> getType() {
        return pointMap;
    }
}
