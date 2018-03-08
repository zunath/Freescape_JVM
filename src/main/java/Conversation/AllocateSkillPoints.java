package Conversation;

import Dialog.*;
import Entities.PlayerEntity;
import Entities.PlayerProgressionSkillEntity;
import Entities.ProgressionSkillEntity;
import Enumerations.QuestID;
import GameObject.PlayerGO;
import GameSystems.QuestSystem;
import Helper.ColorToken;
import Data.Repository.PlayerProgressionSkillsRepository;
import Data.Repository.PlayerRepository;
import Data.Repository.ProgressionSkillRepository;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class AllocateSkillPoints extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select a category.",
                "Statistics",
                "Proficiencies",
                "Utility",
                "Magic",
                "Crafting",
                "Back"
        );

        DialogPage statsPage = new DialogPage(
                "Please select an upgrade.",
                "Hit Points",
                "Mana",
                "Ability Slots",
                "Strength",
                "Dexterity",
                "Constitution",
                "Wisdom",
                "Intelligence",
                "Charisma",
                "Inventory Space",
                "Natural Regeneration",
                "Back"
        );

        DialogPage proficienciesPage = new DialogPage(
                "Please select an upgrade.",
                "Armor Proficiency",
                "Handgun Proficiency",
                "Handgun Accuracy",
                "Shotgun Proficiency",
                "Shotgun Accuracy",
                "Rifle Proficiency",
                "Rifle Accuracy",
                "SMG Proficiency",
                "SMG Accuracy",
                "Back"
        );

        DialogPage utilityPage = new DialogPage(
                "Please select an upgrade.",
                "Search",
                "Hide",
                "Move Silently",
                "First Aid",
                "Lockpicking",
                "Item Repair",
                "Tumble",
                "Back"
        );

        DialogPage magicPage = new DialogPage(
                "Please select an upgrade.",
                "Holy Affinity",
                "Evocation Affinity",
                "Enhancement Affinity",
                "Back"
        );

        DialogPage craftingPage = new DialogPage(
                "Please select an upgrade.",
                "Researching",
                "Back"
        );

        DialogPage upgradePage = new DialogPage(
                "<REPLACED LATER>",
                "Upgrade",
                "Back",
                "Return to Category List"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("StatsPage", statsPage);
        dialog.addPage("ProficienciesPage", proficienciesPage);
        dialog.addPage("UtilityPage", utilityPage);
        dialog.addPage("MagicPage", magicPage);
        dialog.addPage("CraftingPage", craftingPage);
        dialog.addPage("UpgradePage", upgradePage);

        return dialog;
    }

    @Override
    public void Initialize()
    {
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampLevelingUp) == 1)
        {
            QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampLevelingUp);
        }
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName) {
            case "MainPage":
                switch (responseID) {
                    // Statistics
                    case 1:
                        NWScript.setLocalString(oPC, "TEMP_MENU_CATEGORY_PAGE", "StatsPage");
                        ChangePage("StatsPage");
                        break;
                    // Proficiencies
                    case 2:
                        NWScript.setLocalString(oPC, "TEMP_MENU_CATEGORY_PAGE", "ProficienciesPage");
                        ChangePage("ProficienciesPage");
                        break;
                    // Utility
                    case 3:
                        NWScript.setLocalString(oPC, "TEMP_MENU_CATEGORY_PAGE", "UtilityPage");
                        ChangePage("UtilityPage");
                        break;
                    // Magic + Abilities
                    case 4:
                        NWScript.setLocalString(oPC, "TEMP_MENU_CATEGORY_PAGE", "MagicPage");
                        ChangePage("MagicPage");
                        break;
                    // Crafting
                    case 5:
                        NWScript.setLocalString(oPC, "TEMP_MENU_CATEGORY_PAGE", "CraftingPage");
                        ChangePage("CraftingPage");
                        break;
                    case 6: // "Back to Main Menu"
                        ClearTempVariables();
                        SwitchConversation("RestMenu");
                        break;
                }
                break;
            case "StatsPage":
                switch (responseID) {
                    case 1:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_HP);
                        break;
                    case 2:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_MANA);
                        break;
                    case 3:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_ABILITY_SLOTS);
                        break;
                    case 4:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_STRENGTH);
                        break;
                    case 5:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_DEXTERITY);
                        break;
                    case 6:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_CONSTITUTION);
                        break;
                    case 7:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_WISDOM);
                        break;
                    case 8:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_INTELLIGENCE);
                        break;
                    case 9:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_CHARISMA);
                        break;
                    case 10:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_INVENTORY_SPACE);
                        break;
                    case 11:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_NATURAL_REGENERATION);
                        break;
                    case 12: // "Back"
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
            case "ProficienciesPage":
                switch (responseID) {
                    case 1:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_ARMOR);
                        break;
                    case 2:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_HANDGUN_PROFICIENCY);
                        break;
                    case 3:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_HANDGUN_ACCURACY);
                        break;
                    case 4:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_SHOTGUN_PROFICIENCY);
                        break;
                    case 5:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_SHOTGUN_ACCURACY);
                        break;
                    case 6:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_RIFLE_PROFICIENCY);
                        break;
                    case 7:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_RIFLE_ACCURACY);
                        break;
                    case 8:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_SMG_PROFICIENCY);
                        break;
                    case 9:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_SMG_ACCURACY);
                        break;
                    case 10: // "Back"
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
            case "UtilityPage":
                switch (responseID) {
                    case 1:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_SEARCH);
                        break;
                    case 2:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_HIDE);
                        break;
                    case 3:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_MOVE_SILENTLY);
                        break;
                    case 4:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_FIRST_AID);
                        break;
                    case 5:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_LOCKPICKING);
                        break;
                    case 6:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_ITEM_REPAIR);
                        break;
                    case 7:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_TUMBLE);
                        break;
                    case 8: // "Back"
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
            case "MagicPage": {

                switch(responseID) {

                    case 1:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_HOLY_AFFINITY);
                        break;
                    case 2:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_EVOCATION_AFFINITY);
                        break;
                    case 3:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_ENHANCEMENT_AFFINITY);
                        break;
                    case 4: // "Back"
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
            }
            case "CraftingPage": {
                switch (responseID) {
                    case 1:
                        LoadSkillUpgradePage(ProgressionSystem.SkillType_RESEARCHING);
                        break;
                    case 2: // Back
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
            }
            case "UpgradePage":
                switch (responseID) {
                    // Upgrade
                    case 1:
                        HandleUpgrade();
                        ToggleUpgradeResponseOption();
                        break;
                    // Back
                    case 2:
                        NWScript.deleteLocalInt(GetPC(), "TEMP_MENU_SKILL_ID");
                        NWScript.deleteLocalInt(GetPC(), "TEMP_MENU_CONFIRM_PURCHASE");
                        ChangePage(NWScript.getLocalString(GetPC(), "TEMP_MENU_CATEGORY_PAGE"));
                        SetResponseText("UpgradePage", 1, "Upgrade");
                        break;
                    // Return to Category List
                    case 3:
                        ClearTempVariables();
                        ChangePage("MainPage");
                        break;
                }
                break;
        }
    }

    @Override
    public void EndDialog() {
        ClearTempVariables();
    }

    private void ClearTempVariables()
    {
        NWScript.deleteLocalString(GetPC(), "TEMP_MENU_CATEGORY_PAGE");
        NWScript.deleteLocalInt(GetPC(), "TEMP_MENU_SKILL_ID");
        NWScript.deleteLocalInt(GetPC(), "TEMP_MENU_CONFIRM_PURCHASE");
        SetResponseText("UpgradePage", 1, "Upgrade");
    }

    private void HandleUpgrade()
    {
        NWObject oPC = GetPC();
        int skillID = NWScript.getLocalInt(oPC, "TEMP_MENU_SKILL_ID");
        boolean isConfirmation = NWScript.getLocalInt(oPC, "TEMP_MENU_CONFIRM_PURCHASE") == 1;

        if(isConfirmation)
        {
            ProgressionSystem.PurchaseSkillUpgrade(oPC, skillID);
            SetPageHeader("UpgradePage", BuildUpgradeHeader());
            SetResponseText("UpgradePage", 1, "Upgrade");
            NWScript.deleteLocalInt(oPC, "TEMP_MENU_CONFIRM_PURCHASE");
        }
        else
        {
            NWScript.setLocalInt(oPC, "TEMP_MENU_CONFIRM_PURCHASE", 1);
            SetResponseText("UpgradePage", 1, "CONFIRM PURCHASE");
        }

    }

    private void LoadSkillUpgradePage(int skillID)
    {
        NWScript.setLocalInt(GetPC(), "TEMP_MENU_SKILL_ID", skillID);
        SetPageHeader("UpgradePage", BuildUpgradeHeader());
        ToggleUpgradeResponseOption();
        ChangePage("UpgradePage");
    }

    private void ToggleUpgradeResponseOption()
    {
        int skillID = NWScript.getLocalInt(GetPC(), "TEMP_MENU_SKILL_ID");
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerProgressionSkillsRepository pcSkillRepo = new PlayerProgressionSkillsRepository();
        ProgressionSkillRepository skillRepo = new ProgressionSkillRepository();
        PlayerRepository playerRepo = new PlayerRepository();

        PlayerProgressionSkillEntity pcSkill = pcSkillRepo.GetByPlayerIDAndSkillID(pcGO.getUUID(), skillID);
        ProgressionSkillEntity skill = skillRepo.GetProgressionSkillByID(skillID);
        PlayerEntity playerEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        DialogResponse response = GetResponseByID("UpgradePage", 1);
        int upgradeLevel = pcSkill == null ? 0 : pcSkill.getUpgradeLevel();
        int upgradeCap = pcSkill == null || !pcSkill.isSoftCapUnlocked() ? skill.getSoftCap() : skill.getMaxUpgrades();
        int nextUpgradeCost = pcSkill == null ? skill.getInitialPrice() : pcSkill.getUpgradeLevel() + skill.getInitialPrice();

        if(upgradeLevel >= upgradeCap || playerEntity.getUnallocatedSP() < nextUpgradeCost)
        {
            response.setActive(false);
        }
        else
        {
            response.setActive(true);
        }
    }

    private String BuildUpgradeHeader()
    {
        int skillID = NWScript.getLocalInt(GetPC(), "TEMP_MENU_SKILL_ID");
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerRepository pcRepo = new PlayerRepository();
        PlayerProgressionSkillsRepository pcSkillRepo = new PlayerProgressionSkillsRepository();
        ProgressionSkillRepository skillRepo = new ProgressionSkillRepository();

        PlayerEntity pcEntity = pcRepo.GetByPlayerID(pcGO.getUUID());
        PlayerProgressionSkillEntity pcSkill = pcSkillRepo.GetByPlayerIDAndSkillID(pcGO.getUUID(), skillID);
        ProgressionSkillEntity skill = skillRepo.GetProgressionSkillByID(skillID);

        String upgradeName = skill.getName();
        String description = skill.getDescription();
        int upgradeLevel = pcSkill == null ? 0 : pcSkill.getUpgradeLevel();
        int upgradeCap = pcSkill == null || !pcSkill.isSoftCapUnlocked() ? skill.getSoftCap() : skill.getMaxUpgrades();
        int availableSP = pcEntity.getUnallocatedSP();
        int nextUpgradeCost = skill.getInitialPrice() + (pcSkill == null ? 0 : pcSkill.getUpgradeLevel());

        String upgradeCapText = ColorToken.Yellow() + upgradeCap + ColorToken.End();
        if(pcSkill != null && pcSkill.isSoftCapUnlocked())
        {
            upgradeCapText = ColorToken.White() + upgradeCap + ColorToken.End();
        }

        String nextUpgradeCostText = "" + nextUpgradeCost;
        if(upgradeLevel >= upgradeCap)
        {
            nextUpgradeCostText = ColorToken.Red() + "MAX" + ColorToken.End();
        }

        String header = ColorToken.Green() + "Upgrade Name: " + ColorToken.End() + upgradeName + "\n";
        header += ColorToken.Green() + "Upgrade Level: " + ColorToken.End() + upgradeLevel + " / " + upgradeCapText + "\n\n";
        header += ColorToken.Green() + "Available SP: " + ColorToken.End() + availableSP + "\n";
        header += ColorToken.Green() + "Next Upgrade: " + ColorToken.End() + nextUpgradeCostText + "\n\n";
        header += ColorToken.Green() + "Description: " + ColorToken.End() + description;

        return header;
    }
}
