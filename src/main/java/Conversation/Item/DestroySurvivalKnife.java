package Conversation.Item;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class DestroySurvivalKnife extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Are you sure you want to destroy your survival knife? This action is irreversible!",
                "Destroy Survival Knife"
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (responseID)
        {
            case 1: // Destroy item
                NWObject knife = NWScript.getItemPossessedBy(GetPC(), "survival_knife");
                NWScript.destroyObject(knife, 0.0f);
                EndConversation();
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
