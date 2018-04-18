package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import GameSystems.PlayerDescriptionSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ChangeDescription extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "Next",
                "Back"
        );

        DialogPage confirmSetPage = new DialogPage(
                "<SET LATER>",
                "Confirm Description Change",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("ConfirmSetPage", confirmSetPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        String header = "Please type the new description for your character into the chat box. Then press the 'Next' button.\n\n";
        header += ColorToken.Green() + "Current Description: " + ColorToken.End() + "\n\n";
        header += getDescription(GetPC(), false, true);
        SetPageHeader("MainPage", header);
        setLocalInt(GetPC(), "LISTENING_FOR_DESCRIPTION", 1);
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "ConfirmSetPage":
                HandleConfirmSetPageResponse(responseID);
                break;
        }
    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Next
                String newDescription = getLocalString(GetPC(), "NEW_DESCRIPTION_TO_SET");

                if(newDescription.equals(""))
                {
                    floatingTextStringOnCreature("Type in a new description to the chat bar and then press 'Next'.", GetPC(), false);
                    return;
                }

                String header = "Your new description follows. If you need to make a change, click 'Back', type in a new description, and then hit 'Next' again.\n\n";
                header += ColorToken.Green() + "New Description: " + ColorToken.End() + "\n\n";
                header += newDescription;
                SetPageHeader("ConfirmSetPage", header);
                ChangePage("ConfirmSetPage");
                break;
            case 2: // Back
                SwitchConversation("CharacterManagement");
                break;
        }
    }

    private void HandleConfirmSetPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Confirm Description Change
                PlayerDescriptionSystem.ChangePlayerDescription(GetPC());
                EndConversation();
                break;
            case 2: // Back
                ChangePage("MainPage");
                break;
        }
    }

    @Override
    public void EndDialog() {
        deleteLocalInt(GetPC(), "LISTENING_FOR_DESCRIPTION");
        deleteLocalString(GetPC(), "NEW_DESCRIPTION_TO_SET");
    }
}
