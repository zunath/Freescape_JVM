package Conversation;

import Conversation.ViewModels.BackgroundViewModel;
import Dialog.*;
import Entities.BackgroundEntity;
import GameSystems.BackgroundSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

public class BackgroundMenu extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Please select a background out of the following list."
        );

        DialogPage detailsPage = new DialogPage(
                "<SET LATER>",
                "Select Background",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("DetailsPage", detailsPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        BackgroundViewModel model = new BackgroundViewModel();
        SetDialogCustomData(model);
        LoadProfessionOptionsList();
    }

    private void LoadProfessionOptionsList()
    {
        DialogPage page = GetPageByName("MainPage");
        List<BackgroundEntity> entities = BackgroundSystem.GetActiveBackgrounds();

        for(BackgroundEntity entity : entities)
        {
            page.addResponse(entity.getName(), true, entity);
        }
    }


    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {

        switch(pageName)
        {
            case "MainPage":
                HandleMainPageActions(responseID);
                break;
            case "DetailsPage":
                HandleDetailsPageActions(responseID);
                break;
        }
    }

    private BackgroundViewModel GetModel()
    {
        return (BackgroundViewModel)GetDialogCustomData();
    }

    private void HandleMainPageActions(int responseID)
    {
        DialogResponse response = GetResponseByID("MainPage", responseID);
        BackgroundEntity entity = (BackgroundEntity) response.getCustomData();
        BackgroundViewModel model = GetModel();
        model.setSelectedEntity(entity);

        String header = ColorToken.Green() + "Profession Name: " + ColorToken.End() + entity.getName() + "\n";
        header += ColorToken.Green() + "Description: " + ColorToken.End() + entity.getDescription() + "\n\n";
        header += ColorToken.Green() + "Bonuses:" + ColorToken.End() + "\n\n";
        header += entity.getBonuses() + "\n\n";
        header += "Will you select this profession?";

        SetPageHeader("DetailsPage", header);
        ChangePage("DetailsPage");
    }

    private void HandleDetailsPageActions(int responseID)
    {
        NWObject oPC = GetPC();
        BackgroundViewModel model = GetModel();

        switch (responseID)
        {
            case 1: // Select Profession
                if(model.isConfirming())
                {
                    BackgroundSystem.SelectBackground(oPC, model.getSelectedEntity());
                    DialogManager.endConversation(oPC);
                }
                else
                {
                    model.setConfirming(true);
                    SetResponseText("DetailsPage", 1, "CONFIRM SELECT PROFESSION");
                }

                break;
            case 2: // Back
                model.setConfirming(false);
                SetResponseText("DetailsPage", 1, "Select Profession");
                model.setSelectedEntity(null);
                ChangePage("MainPage");
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
