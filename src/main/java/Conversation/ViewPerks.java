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
        PerkLevelEntity currentPerkLevel = FindPerkLevel(perk.getLevels(), rank);
        PerkLevelEntity nextPerkLevel = FindPerkLevel(perk.getLevels(), rank+1);

        if(CanPerkBeUpgraded(perk, pcPerk, player))
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
        if(rank+1 < maxRank)
        {
            if(nextPerkLevel != null)
            {
                nextBonus = nextPerkLevel.getDescription();
                price = nextPerkLevel.getPrice() + " SP (Available: " + player.getUnallocatedSP() + " SP)";
            }
        }

        StringBuilder header = new StringBuilder(ColorToken.Green() + "Name: " + ColorToken.End() + perk.getName() + "\n" +
                ColorToken.Green() + "Rank: " + ColorToken.End() + rank + " / " + maxRank + "\n" +
                ColorToken.Green() + "Price: " + ColorToken.End() + price + "\n" +
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

                if(requirements.size() <= 0)
                {
                    header.append("None\n");
                }

                for(PerkLevelSkillRequirementEntity req: requirements)
                {
                    String detailLine = req.getSkill().getName() + " Rank " + req.getRequiredRank();
                    String colorToken = ColorToken.Red();

                    PCSkillEntity skill = skillRepo.GetPCSkillByID(pcGO.getUUID(), req.getSkill().getSkillID());
                    if(skill.getRank() >= req.getRequiredRank())
                    {
                        colorToken = ColorToken.Green();
                    }

                    header.append(colorToken).append(detailLine).append(ColorToken.End()).append("\n");
                }

            }
        }

        SetPageHeader("PerkDetailsPage", header.toString());

    }

    private PerkLevelEntity FindPerkLevel(List<PerkLevelEntity> levels, int findLevel)
    {
        for(PerkLevelEntity lvl: levels)
        {
            if(lvl.getLevel() == findLevel)
            {
                return lvl;
            }
        }
        return null;
    }

    private boolean CanPerkBeUpgraded(PerkEntity perk, PCPerksEntity pcPerk, PlayerEntity player)
    {
        SkillRepository skillRepo = new SkillRepository();
        int rank = pcPerk == null ? 0 : pcPerk.getPerkLevel();
        int maxRank = perk.getLevels().size();
        if(rank+1 > maxRank) return false;

        PerkLevelEntity level = FindPerkLevel(perk.getLevels(), rank+1);
        if(level == null) return false;

        if(player.getUnallocatedSP() < level.getPrice()) return false;

        for(PerkLevelSkillRequirementEntity req: level.getSkillRequirements())
        {
            PCSkillEntity pcSkill = skillRepo.GetPCSkillByID(player.getPCID(), req.getSkill().getSkillID());
            if(pcSkill.getRank() < req.getRequiredRank()) return false;
        }

        return true;
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
                    DoPerkUpgrade();
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

    private void DoPerkUpgrade()
    {
        PerkMenuViewModel vm = (PerkMenuViewModel)GetDialogCustomData();
        PerkRepository perkRepo = new PerkRepository();
        PlayerRepository playerRepo = new PlayerRepository();

        PlayerGO pcGO = new PlayerGO(GetPC());
        PerkEntity perk = perkRepo.GetPerkByID(vm.getSelectedPerkID());
        PCPerksEntity pcPerk = perkRepo.GetPCPerkByID(pcGO.getUUID(), vm.getSelectedPerkID());
        PlayerEntity player = playerRepo.GetByPlayerID(pcGO.getUUID());

        if(CanPerkBeUpgraded(perk, pcPerk, player))
        {
            if(pcPerk == null)
            {
                pcPerk = new PCPerksEntity();
                DateTime dt = new DateTime(DateTimeZone.UTC);
                pcPerk.setAcquiredDate(new Timestamp(dt.getMillis()));
                pcPerk.setPerk(perk);
                pcPerk.setPlayerID(pcGO.getUUID());
                pcPerk.setPerkLevel(0);
            }

            PerkLevelEntity nextPerkLevel = FindPerkLevel(perk.getLevels(), pcPerk.getPerkLevel()+1);
            if(nextPerkLevel == null) return;

            pcPerk.setPerkLevel(pcPerk.getPerkLevel() + 1);
            player.setUnallocatedSP(player.getUnallocatedSP() - nextPerkLevel.getPrice());

            perkRepo.Save(pcPerk);
            playerRepo.save(player);

            // If a perk is activatable, create the item on the PC.
            // Remove any existing cast spell unique power properties and add the correct one based on the DB flag.
            if(perk.getItemResref() != null && !perk.getItemResref().equals(""))
            {
                if(!NWScript.getIsObjectValid(NWScript.getItemPossessedBy(GetPC(), perk.getItemResref())))
                {
                    NWObject spellItem = NWScript.createItemOnObject(perk.getItemResref(), GetPC(), 1, "");
                    NWScript.setItemCursedFlag(spellItem, true);
                    NWScript.setLocalInt(spellItem, "ACTIVATION_PERK_ID", perk.getPerkID());

                    for(NWItemProperty ip: NWScript.getItemProperties(spellItem))
                    {
                        int ipType = NWScript.getItemPropertyType(ip);
                        int ipSubType = NWScript.getItemPropertySubType(ip);
                        if(ipType == ItemProperty.CAST_SPELL &&
                                (ipSubType == Ip.CONST_CASTSPELL_UNIQUE_POWER ||
                                        ipSubType == Ip.CONST_CASTSPELL_UNIQUE_POWER_SELF_ONLY ||
                                        ipSubType == Ip.CONST_CASTSPELL_ACTIVATE_ITEM ))
                        {
                            NWScript.removeItemProperty(spellItem, ip);
                        }
                    }

                    NWItemProperty ip;
                    if(perk.isTargetSelfOnly()) ip = NWScript.itemPropertyCastSpell(IpConst.CASTSPELL_UNIQUE_POWER_SELF_ONLY, IpConstCastspell.NUMUSES_UNLIMITED_USE);
                    else ip = NWScript.itemPropertyCastSpell(IpConst.CASTSPELL_UNIQUE_POWER, IpConstCastspell.NUMUSES_UNLIMITED_USE);

                    XP2.IPSafeAddItemProperty(spellItem, ip, 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);
                }

                NWScript.setName(NWScript.getItemPossessedBy(GetPC(), perk.getItemResref()), perk.getName() + " (Lvl. " + pcPerk.getPerkLevel() + ")");
            }

            NWScript.sendMessageToPC(GetPC(),ColorToken.Green() + "Perk Purchased: " + perk.getName() + " (Lvl. " + pcPerk.getPerkLevel() + ")");

            IPerk perkScript = (IPerk) ScriptHelper.GetClassByName("Perks." + perk.getJavaScriptName());
            if(perkScript == null) return;
            perkScript.OnPurchased(GetPC(), pcPerk.getPerkLevel());
        }
        else
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You cannot purchase the perk at this time." + ColorToken.End(), GetPC(), false);
        }
    }



    @Override
    public void EndDialog() {

    }
}
