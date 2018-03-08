package Placeable;

import Common.IScriptEventHandler;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class TestBreadAdvanceQuest implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();

        QuestSystem.RequestItemsFromPC(oPC, objSelf, 1, 1);
        //QuestSystem.AdvanceQuestState(oPC, 2);
        //QuestSystem.CompleteQuest(oPC, 1);
    }
}
