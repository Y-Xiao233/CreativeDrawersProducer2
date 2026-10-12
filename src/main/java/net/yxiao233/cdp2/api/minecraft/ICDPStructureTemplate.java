package net.yxiao233.cdp2.api.minecraft;

/**
 * Implemented (through a mixin) by {@code StructureTemplate} so the burning arena can strip the useless top layer of
 * netherrack from its own templates before they are placed.
 */
public interface ICDPStructureTemplate {
    /** Removes the blocks of the highest layer of the template, but only when they are netherrack */
    void cdp2$removeTopNetherrackLayer();
}
