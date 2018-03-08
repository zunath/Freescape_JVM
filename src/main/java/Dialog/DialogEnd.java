package Dialog;

import GameObject.PlayerGO;
import Helper.ErrorHelper;
import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class DialogEnd implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oNPC) {
        NWObject oPC = NWScript.getPCSpeaker();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());

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
    }
}
