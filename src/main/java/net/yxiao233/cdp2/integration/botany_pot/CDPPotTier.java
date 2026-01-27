package net.yxiao233.cdp2.integration.botany_pot;

import com.ultramega.botanypotstiers.common.impl.PotTier;
import org.jetbrains.annotations.Nullable;

public enum CDPPotTier {
    ABSOLUTE("absolute",6,12);
    private final String name;
    private final int outputMultiplier;
    private final int speedMultiplier;
    CDPPotTier(String name, int outputMultiplier, int speedMultiplier){
        this.name = name;
        this.outputMultiplier = outputMultiplier;
        this.speedMultiplier = speedMultiplier;
    }

    public String getName() {
        return name;
    }

    public int getOutputMultiplier() {
        return outputMultiplier;
    }

    public int getSpeedMultiplier() {
        return speedMultiplier;
    }

    public static @Nullable CDPPotTier getPrevious(CDPPotTier current){
        CDPPotTier[] values = values();
        int index = current.ordinal() - 1;
        return index < 0 ? null : values[index];
    }

    public static @Nullable CDPPotTier getNext(PotTier current) {
        CDPPotTier[] values = values();
        int index = current.ordinal() + 1;
        return index > values.length - 1 ? null : values[index];
    }
}
