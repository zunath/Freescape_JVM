package Item;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class XPTome implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject item = NWScript.getItemActivated();

        NWScript.setLocalObject(oPC, "XP_TOME_OBJECT", item);
        Scheduler.assignNow(oPC, () -> NWScript.clearAllActions(false));
        DialogManager.startConversation(oPC, oPC, "XPTome");
    }
}
