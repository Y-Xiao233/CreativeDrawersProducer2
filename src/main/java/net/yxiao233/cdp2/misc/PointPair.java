package net.yxiao233.cdp2.misc;

public class PointPair {
    private final int max;
    private int current;
    private PointPair(int max, int current){
        this.max = max;
        this.current = current;
    }

    public static PointPair of(int max, int point){
        return new PointPair(max,point);
    }

    public static PointPair of(int max){
        return new PointPair(max,0);
    }

    public int getMax() {
        return max;
    }

    public int getPoint() {
        return current;
    }

    public PointPair addPoint(int delta){
        this.current += delta;
        return this;
    }
    public PointPair setPoint(int point){
        this.current = point;
        return this;
    }
}
