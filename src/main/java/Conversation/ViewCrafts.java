package Conversation;

import Dialog.*;
import Entities.CraftEntity;
import Entities.CraftLevelEntity;
import Entities.PCCraftEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.MenuHelper;
import Data.Repository.CraftRepository;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

@SuppressWarnings("unused")
public class ViewCrafts extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select a craft below."
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        LoadMainPageResponses();
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
        }

    }

    @Override
    public void EndDialog() {

    }

    private void LoadMainPageResponses()
    {
        CraftRepository repo = new CraftRepository();
        DialogPage page = GetPageByName("MainPage");
        page.getResponses().clear();
        List<CraftEntity> crafts = repo.GetAllCrafts();

        for(CraftEntity craft : crafts)
        {
            page.addResponse(craft.getName(), true, craft.getCraftID());
        }

        page.addResponse("Back", true);
    }

    private void HandleMainPageResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("MainPage", responseID);
        if(response.getCustomData() == null)
        {
            SwitchConversation("RestMenu");
            return;
        }
        PlayerGO pcGO = new PlayerGO(GetPC());
        int craftID = (int)response.getCustomData();
        CraftRepository repo = new CraftRepository();
        PCCraftEntity pcCraft = repo.GetPCCraftByID(pcGO.getUUID(), craftID);
        CraftEntity craft = repo.GetCraftByID(craftID);
        CraftLevelEntity level = repo.GetCraftLevelByLevel(craftID, pcCraft.getLevel());

        String header = ColorToken.Green() + "Craft: " + ColorToken.End() + craft.getName() + "\n\n";
        header += ColorToken.Green() + "Level: " + ColorToken.End() + pcCraft.getLevel() + "\n";
        header += ColorToken.Green() + "EXP: " + ColorToken.End() + MenuHelper.BuildBar(pcCraft.getExperience(), level.getExperience(), 100) + "\n\n";
        header += ColorToken.Green() + "Description: " + ColorToken.End() + craft.getDescription();

        SetPageHeader("MainPage", header);
    }
}
