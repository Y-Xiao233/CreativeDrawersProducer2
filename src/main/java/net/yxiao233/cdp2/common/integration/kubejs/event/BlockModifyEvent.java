package net.yxiao233.cdp2.common.integration.kubejs.event;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.rhino.util.HideFromJS;

import java.util.ArrayList;
import java.util.List;

public class BlockModifyEvent implements KubeEvent {
    @HideFromJS
    public static final List<String> WHITE_LIST = new ArrayList<>();
    public static void addToModDuplicatorWhiteList(String id){
        if(!WHITE_LIST.contains(id)){
            WHITE_LIST.add(id);
        }
    }
}
