package Dialog;

import Common.IScriptEventHandler;
import GameObject.PlayerGO;
import Helper.ErrorHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class AppearsWhen implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oNPC) {
        NWObject oPC = NWScript.getPCSpeaker();
        PlayerGO pcGO = new PlayerGO(oPC);
        String uuid = pcGO.getUUID();
        PlayerDialog dialog = DialogManager.loadPlayerDialog(uuid);
        DialogPage page = dialog.getCurrentPage();
        int nodeID = NWScript.getLocalInt(oNPC, "APPEARS_WHEN_NODE");
        int currentSelectionNumber = nodeID + 1;
        int nodeType = NWScript.getLocalInt(oNPC, "APPEARS_WHEN_TYPE");
        int gender = NWScript.getGender(oPC);
        boolean displayNode = false;
        String newNodeText = "";
        int dialogOffset = (DialogManager.NumberOfResponsesPerPage + 1) * (dialog.getDialogNumber() - 1);

        if(currentSelectionNumber == DialogManager.NumberOfResponsesPerPage + 1) // Next Page
        {
            int displayCount = page.getNumberOfResponses() - (DialogManager.NumberOfResponsesPerPage * dialog.getPageOffset());

            if(displayCount > DialogManager.NumberOfResponsesPerPage)
            {
                displayNode = true;
            }
        }
        else if(currentSelectionNumber == DialogManager.NumberOfResponsesPerPage + 2) // Previous Page
        {
            if(dialog.getPageOffset() > 0)
            {
                displayNode = true;
            }
        }
        else if(nodeType == 2)
        {
            int responseID = (dialog.getPageOffset() * DialogManager.NumberOfResponsesPerPage) + nodeID;

            if(responseID + 1 <= page.getNumberOfResponses()) {
                DialogResponse response = page.getResponses().get(responseID);

                if (response != null) {
                    newNodeText = response.getText();
                    displayNode = response.isActive();
                }
            }
        }
        else if(nodeType == 1)
        {
            if(NWScript.getLocalInt(oPC, "DIALOG_SYSTEM_INITIALIZE_RAN") != 1)
            {
                try
                {
                    Class scriptClass = Class.forName("Conversation." + dialog.getActiveDialogName());
                    IDialogHandler script = (IDialogHandler)scriptClass.newInstance();
                    script.Initialize();
                    NWScript.setLocalInt(oPC, "DIALOG_SYSTEM_INITIALIZE_RAN", 1);
                }
                catch (Exception ex)
                {
                    ErrorHelper.HandleException(ex, "Unable to initialize conversation: " + dialog.getActiveDialogName());
                }
            }

            if(dialog.isEnding())
            {
                try {
                    Class scriptClass = Class.forName("Conversation." + dialog.getActiveDialogName());
                    IDialogHandler script = (IDialogHandler)scriptClass.newInstance();
                    script.EndDialog();
                    DialogManager.removePlayerDialog(pcGO.getUUID());
                }
                catch(Exception ex) {
                    ErrorHelper.HandleException(ex, "DialogEnd was unable to execute class method.");
                }

                NWScript.deleteLocalInt(oPC, "DIALOG_SYSTEM_INITIALIZE_RAN");
                NWScript.setLocalInt(oNPC, "CONVERSATION_SHOW_NODE", 0);
                return;
            }

            page = dialog.getCurrentPage();
            newNodeText = page.getHeader();


            NWScript.setCustomToken(90000 + dialogOffset, newNodeText);
            NWScript.setLocalInt(oNPC, "CONVERSATION_SHOW_NODE", 1);
            return;
        }

        NWScript.setCustomToken(90001 + nodeID + dialogOffset, newNodeText);
        NWScript.setLocalInt(oNPC, "CONVERSATION_SHOW_NODE", displayNode ? 1 : 0);

    }
}
