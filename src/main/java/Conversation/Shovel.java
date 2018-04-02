package Conversation;

import Dialog.DialogBase;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Shovel extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {

    }

    @Override
    public void EndDialog() {

        deleteLocalObject(GetPC(), "SHOVEL_ITEM");
    }
}
