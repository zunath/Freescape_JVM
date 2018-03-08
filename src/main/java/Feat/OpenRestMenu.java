package Feat;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWObject;

public class OpenRestMenu implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        DialogManager.startConversation(oPC, oPC, "RestMenu");
    }
}
