package Conversation;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Conversation.ViewModels.PerkMenuViewModel;
import Data.Repository.PerkRepository;
import Data.Repository.PlayerRepository;
import Data.Repository.SkillRepository;
import Dialog.*;
import Entities.*;
import GameObject.PlayerGO;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import Helper.ScriptHelper;
import Perks.IPerk;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Ip;
import org.nwnx.nwnx2.jvm.constants.IpConst;
import org.nwnx.nwnx2.jvm.constants.IpConstCastspell;
import org.nwnx.nwnx2.jvm.constants.ItemProperty;

import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("unused")
public class ViewPerks extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "View My Perks",
                "Buy Perks",
                "Back"
        );

        DialogPage categoryPage = new DialogPage(
                "Please select a category. Additional options will appear as you increase your skill ranks."
        );

        DialogPage perkListPage = new DialogPage(
                "Please select a perk. Additional options will appear as you increase your skill ranks."
        );

        DialogPage perkDetailsPage = new DialogPage(
                "<SET LATER>",
                "Purchase Upgrade",
                "Back"
        );

        DialogPage viewMyPerksPage = new DialogPage(
                "<SET LATER>",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("CategoryPage", categoryPage);
        dialog.addPage("PerkListPage", perkListPage);
        dialog.addPage("PerkDetailsPage", perkDetailsPage);
        dialog.addPage("ViewMyPerksPage", viewMyPerksPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        PerkMenuViewModel vm = new PerkMenuViewModel();
        SetPageHeader("MainPage", GetMainPageHeader());
        SetDialogCustomData(vm);
    }

    private String GetMainPageHeader()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerRepository playerRepo = new PlayerRepository();
        SkillRepository skillRepo = new SkillRepository();
        PerkRepository perkRepo = new PerkRepository();
        PlayerEntity pcEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        int totalSP = skillRepo.GetPCTotalSkillCount(pcGO.getUUID());
        int totalPerks = perkRepo.GetPCTotalPerkCount(pcGO.getUUID());

        return ColorToken.Green() + "Total SP: " + ColorToken.End() + totalSP + " / " + SkillSystem.SkillCap + "\n" +
                ColorToken.Green() + "Available SP: " + ColorToken.End() + pcEntity.getUnallocatedSP() + "\n" +
                ColorToken.Green() + "Total Perks: " + ColorToken.End() + totalPerks + "\n";
    }

    private void BuildViewMyPerks()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PerkRepository perkRepo = new PerkRepository();
        List<PCPerkHeaderEntity> perks = perkRepo.GetPCPerksForMenuHeader(pcGO.getUUID());

        StringBuilder header = new StringBuilder(ColorToken.Green() + "Perks purchased:" + ColorToken.End() + "\n\n");
        for(PCPerkHeaderEntity perk: perks)
        {
            header.append(perk.getName()).append(" (Lvl. ").append(perk.getLevel()).append(") ").append("\n");
        }

        SetPageHeader("ViewMyPerksPage", header.toString());
    }

    private void BuildCategoryList()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PerkRepository perkRepo = new PerkRepository();
        List<PerkCategoryEntity> categories = perkRepo.GetPerkCategoriesForPC(pcGO.getUUID());

        ClearPageResponses("CategoryPage");
        for(PerkCategoryEntity category: categories)
        {
            AddResponseToPage("CategoryPage", category.getName(), true, category.getPerkCategoryID());
        }
        AddResponseToPage("CategoryPage", "Back", true, -1);
    }

    private void BuildPerkList()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();
        PerkRepository perkRepo = new PerkRepository();
        List<PerkEntity> perks = perkRepo.GetPerksForPC(pcGO.getUUID(), vm.getSelectedCategoryID());

        ClearPageResponses("PerkListPage");
        for(PerkEntity perk: perks)
        {
            AddResponseToPage("PerkListPage", perk.getName(), true, perk.getPerkID());
        }
        AddResponseToPage("PerkListPage", "Back", true, -1);
    }

    private void BuildPerkDetails()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();
        PerkRepository perkRepo = new PerkRepository();
        PerkEntity perk = perkRepo.GetPerkByID(vm.getSelectedPerkID());
        PCPerksEntity pcPerk = perkRepo.GetPCPerkByID(pcGO.getUUID(), perk.getPerkID());
        PlayerRepository playerRepo = new PlayerRepository();
        PlayerEntity player = playerRepo.GetByPlayerID(pcGO.getUUID());

        int rank = pcPerk == null ? 0 : pcPerk.getPerkLevel();
        int maxRank = perk.getLevels().size();
        String currentBonus = "N/A";
        String nextBonus = "N/A";
        String price = "N/A";
        PerkLevelEntity currentPerkLevel = PerkSystem.FindPerkLevel(perk.getLevels(), rank);
        PerkLevelEntity nextPerkLevel = PerkSystem.FindPerkLevel(perk.getLevels(), rank+1);

        if(PerkSystem.CanPerkBeUpgraded(perk, pcPerk, player))
        {
            SetResponseVisible("PerkDetailsPage", 1, true);
        }
        else
        {
            SetResponseVisible("PerkDetailsPage", 1, false);
        }

        if(rank > 0)
        {
            if(currentPerkLevel != null)
            {
                currentBonus = currentPerkLevel.getDescription();
            }
        }
        if(rank+1 <= maxRank)
        {
            if(nextPerkLevel != null)
            {
                nextBonus = nextPerkLevel.getDescription();
                price = nextPerkLevel.getPrice() + " SP (Available: " + player.getUnallocatedSP() + " SP)";
            }
        }

        StringBuilder header = new StringBuilder(ColorToken.Green() + "Name: " + ColorToken.End() + perk.getName() + "\n" +
                ColorToken.Green() + "Category: " + ColorToken.End() + perk.getCategory().getName() + "\n" +
                ColorToken.Green() + "Rank: " + ColorToken.End() + rank + " / " + maxRank + "\n" +
                ColorToken.Green() + "Price: " + ColorToken.End() + price + "\n" +
                (perk.getBaseManaCost() > 0 ? ColorToken.Green() + "Mana: " + ColorToken.End() + perk.getBaseManaCost() : "") + "\n" +
                (perk.getCooldown() != null && perk.getCooldown().getBaseCooldownTime() > 0 ? ColorToken.Green() + "Cooldown: " + ColorToken.End() + perk.getCooldown().getBaseCooldownTime() + "s" : "") + "\n" +
                ColorToken.Green() + "Description: " + ColorToken.End() + perk.getDescription() + "\n" +
                ColorToken.Green() + "Current Bonus: " + ColorToken.End() + currentBonus + "\n" +
                ColorToken.Green() + "Next Bonus: " + ColorToken.End() + nextBonus + "\n");

        if(nextPerkLevel != null)
        {
            List<PerkLevelSkillRequirementEntity> requirements = nextPerkLevel.getSkillRequirements();
            if(requirements.size() > 0)
            {
                SkillRepository skillRepo = new SkillRepository();
                header.append("\n").append(ColorToken.Green()).append("Next Upgrade Skill Requirements:").append(ColorToken.End()).append("\n\n");

                boolean hasRequirement = false;
                for(PerkLevelSkillRequirementEntity req: requirements)
                {
                    if(req.getRequiredRank() > 0)
                    {
                        String detailLine = req.getSkill().getName() + " Rank " + req.getRequiredRank();
                        String colorToken = ColorToken.Red();

                        PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), req.getSkill().getSkillID());
                        if(skill.getRank() >= req.getRequiredRank())
                        {
                            colorToken = ColorToken.Green();
                        }

                        header.append(colorToken).append(detailLine).append(ColorToken.End()).append("\n");
                        hasRequirement = true;
                    }
                }

                if(requirements.size() <= 0 || !hasRequirement)
                {
                    header.append("None\n");
                }
            }
        }

        SetPageHeader("PerkDetailsPage", header.toString());

    }


    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponses(responseID);
                break;
            case "ViewMyPerksPage":
                HandleViewMyPerksResponses(responseID);
                break;
            case "CategoryPage":
                HandleCategoryResponses(responseID);
                break;
            case "PerkListPage":
                HandlePerkListResponses(responseID);
                break;
            case "PerkDetailsPage":
                HandlePerkDetailsResponses(responseID);
                break;

        }
    }

    private void HandleMainPageResponses(int responseID)
    {
        switch (responseID)
        {
            case 1: // View My Perks
                BuildViewMyPerks();
                ChangePage("ViewMyPerksPage");
                break;
            case 2: // Buy Perks
                BuildCategoryList();
                ChangePage("CategoryPage");
                break;
            case 3: // Back
                SwitchConversation("RestMenu");
                break;
        }
    }

    private void HandleViewMyPerksResponses(int responseID)
    {
        ChangePage("MainPage");
    }

    private void HandleCategoryResponses(int responseID)
    {
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();
        DialogResponse response = GetResponseByID("CategoryPage", responseID);
        if((int)response.getCustomData() == -1)
        {
            ChangePage("MainPage");
            return;
        }

        vm.setSelectedCategoryID((int)response.getCustomData());
        BuildPerkList();
        ChangePage("PerkListPage");
    }

    private void HandlePerkListResponses(int responseID)
    {
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();
        DialogResponse response = GetResponseByID("PerkListPage", responseID);
        if((int)response.getCustomData() == -1)
        {
            ChangePage("CategoryPage");
            return;
        }

        vm.setSelectedPerkID((int)response.getCustomData());
        BuildPerkDetails();
        ChangePage("PerkDetailsPage");
    }

    private void HandlePerkDetailsResponses(int responseID)
    {
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();

        switch(responseID)
        {
            case 1: // Purchase Upgrade / Confirm Purchase
                if(vm.isConfirmingPurchase())
                {
                    SetResponseText("PerkDetailsPage", 1, "Purchase Upgrade");
                    PerkSystem.DoPerkUpgrade(GetPC(), vm.getSelectedPerkID());
                    vm.setConfirmingPurchase(false);
                    BuildPerkDetails();
                }
                else
                {
                    vm.setConfirmingPurchase(true);
                    SetResponseText("PerkDetailsPage", 1, "CONFIRM PURCHASE");
                }
                break;
            case 2: // Back
                vm.setConfirmingPurchase(false);
                SetResponseText("PerkDetailsPage", 1, "Purchase Upgrade");
                BuildPerkList();
                ChangePage("PerkListPage");
                break;
        }
    }



    @Override
    public void EndDialog() {

    }
}
