package Conversation;

import Conversation.ViewModels.SkillMenuViewModel;
import Data.Repository.SkillRepository;
import Dialog.*;
import Entities.PCSkillEntity;
import Entities.SkillCategoryEntity;
import Entities.SkillEntity;
import Entities.SkillXPRequirementEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
import Helper.MenuHelper;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.List;

public class ViewSkills extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("CategoryPage");
        DialogPage mainPage = new DialogPage(
                "Please select a skill category."
        );

        DialogPage skillListPage = new DialogPage(
                "Please select a skill."
        );

        DialogPage skillDetailsPage = new DialogPage(
                "<SET LATER>",
                "Toggle Decay Lock",
                "Back"
        );

        dialog.addPage("CategoryPage", mainPage);
        dialog.addPage("SkillListPage", skillListPage);
        dialog.addPage("SkillDetailsPage", skillDetailsPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        SetDialogCustomData(new SkillMenuViewModel());
        LoadCategoryResponses();
    }

    private void LoadCategoryResponses()
    {
        SkillRepository repo = new SkillRepository();
        List<SkillCategoryEntity> categories = repo.GetActiveCategories();

        ClearPageResponses("CategoryPage");
        for(SkillCategoryEntity category : categories)
        {
            AddResponseToPage("CategoryPage", category.getName(), true, category.getSkillCategoryID());
        }
        AddResponseToPage("CategoryPage", "Back", true, -1);
    }

    private void LoadSkillResponses()
    {
        SkillMenuViewModel vm = (SkillMenuViewModel)GetDialogCustomData();
        SkillRepository repo = new SkillRepository();
        List<SkillEntity> skills = repo.GetActiveSkillsForCategory(vm.getSelectedCategoryID());

        ClearPageResponses("SkillListPage");
        for(SkillEntity skill : skills)
        {
            AddResponseToPage("SkillListPage", skill.getName(), true, skill.getSkillID());
        }
        AddResponseToPage("SkillListPage", "Back", true, -1);
    }

    private void LoadSkillDetails()
    {
        SkillMenuViewModel vm = (SkillMenuViewModel)GetDialogCustomData();
        PlayerGO pcGO = new PlayerGO(GetPC());
        SkillRepository repo = new SkillRepository();
        PCSkillEntity pcSkill = repo.GetPCSkillByID(pcGO.getUUID(), vm.getSelectedSkillID());
        SkillXPRequirementEntity req = repo.GetSkillXPRequirementByRank(vm.getSelectedSkillID(), pcSkill.getRank());
        SetPageHeader("SkillDetailsPage", CreateSkillDetailsHeader(pcSkill, req));
    }

    private String CreateSkillDetailsHeader(PCSkillEntity pcSkill, SkillXPRequirementEntity req)
    {
        String title;
        if(pcSkill.getRank() <= 3) title = "Untrained";
        else if(pcSkill.getRank() <= 7) title = "Neophyte";
        else if(pcSkill.getRank() <= 13) title = "Novice";
        else if(pcSkill.getRank() <= 20) title = "Apprentice";
        else if(pcSkill.getRank() <= 35) title = "Journeyman";
        else if(pcSkill.getRank() <= 50) title = "Expert";
        else if(pcSkill.getRank() <= 65) title = "Adept";
        else if(pcSkill.getRank() <= 80) title = "Master";
        else if(pcSkill.getRank() <= 100) title = "Grandmaster";
        else title = "Unknown";

        title += " (" + pcSkill.getRank() + ")";

        String decayLock = ColorToken.White() + "Unlocked" + ColorToken.End();
        if(pcSkill.isLocked())
        {
            decayLock = ColorToken.Red() + "Locked" + ColorToken.End();
        }

        return
                ColorToken.Green() + "Skill: " + ColorToken.End() + pcSkill.getSkill().getName() + "\n" +
                ColorToken.Green() + "Rank: " + ColorToken.End() + title + "\n" +
                ColorToken.Green() + "Exp: " + ColorToken.End() + MenuHelper.BuildBar(pcSkill.getXp(), req.getXp(), 100, ColorToken.Orange()) + "\n" +
                ColorToken.Green() + "Decay Lock: " + ColorToken.End() + decayLock + "\n\n" +
                ColorToken.Green() + "Description: " + ColorToken.End() + pcSkill.getSkill().getDescription() + "\n";
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "CategoryPage":
                HandleCategoryResponse(responseID);
                break;
            case "SkillListPage":
                HandleSkillListResponse(responseID);
                break;
            case "SkillDetailsPage":
                HandleSkillDetailsResponse(responseID);
                break;
        }
    }

    private void HandleCategoryResponse(int responseID) {
        SkillMenuViewModel vm = (SkillMenuViewModel) GetDialogCustomData();
        DialogResponse response = GetResponseByID("CategoryPage", responseID);
        int categoryID = (int) response.getCustomData();
        if (categoryID == -1) // Back
        {
            SwitchConversation("RestMenu");
            return;
        }

        vm.setSelectedCategoryID(categoryID);
        LoadSkillResponses();
        ChangePage("SkillListPage");
    }

    private void HandleSkillListResponse(int responseID)
    {
        SkillMenuViewModel vm = (SkillMenuViewModel)GetDialogCustomData();
        DialogResponse response = GetResponseByID("SkillListPage", responseID);
        int skillID = (int)response.getCustomData();
        if(skillID == -1) // Back
        {
            ChangePage("CategoryPage");
            return;
        }

        vm.setSelectedSkillID(skillID);
        LoadSkillDetails();
        ChangePage("SkillDetailsPage");
    }

    private void HandleSkillDetailsResponse(int responseID)
    {
        SkillMenuViewModel vm = (SkillMenuViewModel)GetDialogCustomData();
        PlayerGO pcGO = new PlayerGO(GetPC());
        SkillRepository repo = new SkillRepository();
        PCSkillEntity pcSkill = repo.GetPCSkillByID(pcGO.getUUID(), vm.getSelectedSkillID());

        switch(responseID)
        {
            case 1: // Toggle Lock
                pcSkill.setLocked(!pcSkill.isLocked());
                repo.Save(pcSkill);
                LoadSkillDetails();
                break;
            case 2: // Back
                ChangePage("SkillListPage");
                break;
        }

    }

    @Override
    public void EndDialog() {

    }
}
