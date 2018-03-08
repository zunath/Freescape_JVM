package Dialog;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class DialogStart implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        String className = NWScript.getLocalString(oPC, "CONVERSATION");
        NWObject oTalkToTarget = NWScript.getLocalObject(oPC, "CONVERSATION_TARGET");
        NWScript.deleteLocalString(oPC, "CONVERSATION");
        NWScript.deleteLocalObject(oPC, "CONVERSATION_TARGET");

        DialogManager.startConversation(oPC, oTalkToTarget, className);
    }
}
