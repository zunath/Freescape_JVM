package Conversation;

import Conversation.ViewModels.XPTomeViewModel;
import Data.Repository.SkillRepository;
import Dialog.*;
import Entities.PCSkillEntity;
import Entities.SkillCategoryEntity;
import GameObject.PlayerGO;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;

public class XPTome extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("CategoryPage");

        DialogPage categoryPage = new DialogPage(
                "This tome holds techniques lost to the ages. Select a skill to earn experience in that art."
        );

        DialogPage skillListPage = new DialogPage(
                "This tome holds techniques lost to the ages. Select a skill to earn experience in that art."
        );

        DialogPage confirmPage = new DialogPage(
                "<SET LATER>",
                "Select this skill.",
                "Back"
        );

        dialog.addPage("CategoryPage", categoryPage);
        dialog.addPage("SkillListPage", skillListPage);
        dialog.addPage("ConfirmPage", confirmPage);

        return dialog;
    }

    @Override
    public void Initialize() {
        SkillRepository skillRepo = new SkillRepository();
        List<SkillCategoryEntity> categories = skillRepo.GetActiveCategories();

        for(SkillCategoryEntity category: categories)
        {
            AddResponseToPage("CategoryPage", category.getName(), true, category.getSkillCategoryID());
        }

        XPTomeViewModel vm = new XPTomeViewModel();
        vm.setItem(NWScript.getLocalObject(GetPC(), "XP_TOME_OBJECT"));
        SetDialogCustomData(vm);
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "CategoryPage":
                HandleCategoryPageResponse(responseID);
                break;
            case "SkillListPage":
                HandleSkillListResponse(responseID);
                break;
            case "ConfirmPage":
                HandleConfirmPageResponse(responseID);
                break;
        }
    }

    private void HandleCategoryPageResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("CategoryPage", responseID);
        int categoryID = (int)response.getCustomData();

        PlayerGO pcGO = new PlayerGO(GetPC());
        SkillRepository skillRepo = new SkillRepository();
        List<PCSkillEntity> pcSkills = skillRepo.GetPCSkillsForCategory(pcGO.getUUID(), categoryID);

        ClearPageResponses("SkillListPage");
        for(PCSkillEntity pcSkill: pcSkills)
        {
            AddResponseToPage("SkillListPage", pcSkill.getSkill().getName(), true, pcSkill.getSkill().getSkillID());
        }
        AddResponseToPage("SkillListPage", "Back", true, -1);

        ChangePage("SkillListPage");
    }

    private void HandleSkillListResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("SkillListPage", responseID);
        int skillID = (int)response.getCustomData();
        if(skillID == -1)
        {
            ChangePage("CategoryPage");
            return;
        }

        PlayerGO pcGO = new PlayerGO(GetPC());
        SkillRepository skillRepo = new SkillRepository();
        PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(pcGO.getUUID(), skillID);
        String header = "Are you sure you want to improve your " + pcSkill.getSkill().getName() + " skill?";
        SetPageHeader("ConfirmPage", header);

        XPTomeViewModel vm = (XPTomeViewModel)GetDialogCustomData();
        vm.setSkillID(skillID);
        SetDialogCustomData(vm);
        ChangePage("ConfirmPage");
    }

    private void HandleConfirmPageResponse(int responseID)
    {
        if(responseID == 2)
        {
            ChangePage("SkillListPage");
            return;
        }

        XPTomeViewModel vm = (XPTomeViewModel)GetDialogCustomData();

        if(vm.getItem() != null && NWScript.getIsObjectValid(vm.getItem()))
        {
            int xp = NWScript.getLocalInt(vm.getItem(), "XP_TOME_AMOUNT");
            SkillSystem.GiveSkillXP(GetPC(), vm.getSkillID(), xp);
            NWScript.destroyObject(vm.getItem(), 0.0f);
        }

        EndConversation();
    }


    @Override
    public void EndDialog() {

    }
}
