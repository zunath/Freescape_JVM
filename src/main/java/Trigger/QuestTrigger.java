package Trigger;

import Common.IScriptEventHandler;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class QuestTrigger implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        QuestSystem.OnQuestTriggerEntered(objSelf);
    }
}
