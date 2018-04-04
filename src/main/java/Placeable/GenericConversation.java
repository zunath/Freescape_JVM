package Placeable;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class GenericConversation implements IScriptEventHandler {
    @Override
    public void runScript(NWObject placeable) {
        NWObject oPC = getLastUsedBy();
        if(!getIsPC(oPC)) return;

        String conversation = getLocalString(placeable, "CONVERSATION");
        DialogManager.startConversation(oPC, placeable, conversation);
    }
}
