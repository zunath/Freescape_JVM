package Placeable.QuestSystem;

import Common.IScriptEventHandler;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        QuestSystem.OnQuestPlaceableUsed(objSelf);
    }
}
