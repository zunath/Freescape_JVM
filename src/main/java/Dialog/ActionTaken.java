package Dialog;

import GameObject.PlayerGO;
import Helper.ErrorHelper;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class ActionTaken implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oNPC) {
        NWObject oPC = NWScript.getPCSpeaker();
        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        PlayerDialog dialog = DialogManager.loadPlayerDialog(uuid);
        int nodeID = NWScript.getLocalInt(oNPC, "ACTIONS_TAKEN_NODE");
        int selectionNumber = nodeID + 1;
        int responseID = nodeID + (DialogManager.NumberOfResponsesPerPage * dialog.getPageOffset());

        if(selectionNumber == DialogManager.NumberOfResponsesPerPage + 1) // Next Page
        {
            dialog.setPageOffset(dialog.getPageOffset() + 1);
        }
        else if(selectionNumber == DialogManager.NumberOfResponsesPerPage + 2) // Previous Page
        {
            dialog.setPageOffset(dialog.getPageOffset() - 1);
        }
        else if(selectionNumber != DialogManager.NumberOfResponsesPerPage + 3) // End
        {

            // Try to locate a matching class name based on the event passed in from NWN JVM_EVENT call.
            try {
                Class scriptClass = Class.forName("Conversation." + dialog.getActiveDialogName());
                IDialogHandler script = (IDialogHandler) scriptClass.newInstance();
                script.DoAction(oPC, dialog.getCurrentPageName(), responseID + 1);
            } catch (Exception ex) {
                ErrorHelper.HandleException(ex, "ActionTaken was unable to execute class method: Conversation.\" + dialog.getActiveDialogName() + \".DoAction()");
            }
        }

    }
}
