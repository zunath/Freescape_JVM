package Conversation;

import Conversation.ViewModels.ProfessionViewModel;
import Dialog.*;
import Entities.ProfessionEntity;
import GameSystems.ProfessionSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

public class ProfessionMenu extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Please select a profession out of the following list."
        );

        DialogPage detailsPage = new DialogPage(
                "<SET LATER>",
                "Select Profession",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("DetailsPage", detailsPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        ProfessionViewModel model = new ProfessionViewModel();
        SetDialogCustomData(model);
        LoadProfessionOptionsList();
    }

    private void LoadProfessionOptionsList()
    {
        DialogPage page = GetPageByName("MainPage");
        List<ProfessionEntity> entities = ProfessionSystem.GetActiveProfessions();

        for(ProfessionEntity entity : entities)
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

    private ProfessionViewModel GetModel()
    {
        return (ProfessionViewModel)GetDialogCustomData();
    }

    private void HandleMainPageActions(int responseID)
    {
        DialogResponse response = GetResponseByID("MainPage", responseID);
        ProfessionEntity entity = (ProfessionEntity) response.getCustomData();
        ProfessionViewModel model = GetModel();
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
        ProfessionViewModel model = GetModel();

        switch (responseID)
        {
            case 1: // Select Profession
                if(model.isConfirming())
                {
                    ProfessionSystem.SelectProfession(oPC, model.getSelectedEntity());
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
