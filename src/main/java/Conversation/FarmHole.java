package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FarmHole extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "There is a small hole here. What would you like to do?",
                "Plant a seed",
                "Cover up the hole"
        );

        DialogPage coverUpConfirm = new DialogPage(
                "Are you sure you want to cover up this hole? You'll need a shovel to dig it up again!",
                "Yes, cover up the hole",
                "Cancel"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("CoverUpConfirm", coverUpConfirm);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "CoverUpConfirm":
                HandleCoverUpConfirmResponse(responseID);
                break;
        }
    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Plant a seed
                NWLocation location = getLocation(GetPC());
                NWObject planter = createObject(ObjectType.PLACEABLE, "farm_plant_seed", location, false, "");
                setLocalObject(planter, "FARM_SMALL_HOLE", GetDialogTarget());
                Scheduler.assign(GetPC(), () -> actionInteractObject(planter));
                break;
            case 2: // Cover up the hole
                ChangePage("CoverUpConfirm");
                break;
        }
    }

    private void HandleCoverUpConfirmResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Confirm
                destroyObject(GetDialogTarget(), 0.0f);
                EndConversation();
                break;
            case 2: // Cancel
                ChangePage("MainPage");
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
