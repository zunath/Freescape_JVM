package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import GameSystems.DeathSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class BindSoul extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "If you die, you will respawn at the last place you bound your soul. Would you like to bind your soul to this location?",
                "Bind my soul"
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        if(responseID == 1)
        {
            DeathSystem.BindSoul(oPC, true);
            EndConversation();
        }
    }

    @Override
    public void EndDialog() {

    }
}
