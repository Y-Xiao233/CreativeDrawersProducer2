package net.yxiao233.cdp2.common.integration.ftbquests;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import net.yxiao233.cdp2.CreativeDrawersProducer2;

public interface CDPTaskTypes {
    TaskType CHEMICAL = TaskTypes.register(CreativeDrawersProducer2.makeId("chemical"),ChemicalQuestTask::new,() ->{
        return Icon.getIcon("cdp2:textures/icon/chemical_detection_monitor.png");
    });
    static void init(){

    }
}
