package Creature;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class GenericOnConversation implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastSpeaker();
        if(!NWScript.getIsPC(oPC)) return;

        String conversationName = NWScript.getLocalString(objSelf, "CONVERSATION");
        DialogManager.startConversation(oPC, objSelf, conversationName);
    }
}
