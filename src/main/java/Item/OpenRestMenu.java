package Item;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class OpenRestMenu implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        Scheduler.assignNow(oPC, () -> NWScript.clearAllActions(false));
        DialogManager.startConversation(oPC, oPC, "RestMenu");
    }
}
