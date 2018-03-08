package Conversation;

import Conversation.ViewModels.AdjustAbilitiesViewModel;
import Data.Repository.MagicRepository;
import Dialog.*;
import Entities.AbilityCategoryEntity;
import Entities.AbilityEntity;
import Entities.PCEquippedAbilityEntity;
import Entities.PCLearnedAbilityEntity;
import Enumerations.QuestID;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import GameSystems.QuestSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;
import java.util.Objects;

public class AdjustAbilities extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "The cybernetic amplifier embedded into your brain allows you to equip abilities. All abilities must be installed by using Ability Discs. Once installed you may equip them here at this terminal.",
                "Equip Abilities",
                "Unequip Abilities"

        );

        DialogPage selectSlotPage = new DialogPage(
                "Please select a slot. Additional ability slots can purchased with Skill Points."
        );

        DialogPage selectCategoryPage = new DialogPage(
                "Please select an ability category."
        );

        DialogPage selectAbilityPage = new DialogPage(
                "Select an ability to equip."
        );

        DialogPage confirmAbilityPage = new DialogPage(
                "<SET LATER>",
                "Equip this ability",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("SelectSlotPage", selectSlotPage);
        dialog.addPage("SelectCategoryPage", selectCategoryPage);
        dialog.addPage("SelectAbilityPage", selectAbilityPage);
        dialog.addPage("ConfirmAbilityPage", confirmAbilityPage);

        return dialog;
    }

    @Override
    public void Initialize() {
        NWObject oPC = GetPC();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC))
        {
            NWScript.sendMessageToPC(oPC, "Only player characters may use this terminal.");
            EndConversation();
            return;
        }

        MagicRepository repo = new MagicRepository();
        AdjustAbilitiesViewModel model = new AdjustAbilitiesViewModel();

        PlayerGO pcGO = new PlayerGO(oPC);
        model.setNumberOfAbilitySlots(ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ABILITY_SLOTS) + 1);
        model.setEquippedAbilities(repo.GetPCEquippedAbilities(pcGO.getUUID()));
        model.setCategories(repo.GetActiveAbilityCategories());
        SetDialogCustomData(model);

        LoadSlotPageResponses();
        LoadCategoryPageResponses();
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "SelectSlotPage":
                HandleSelectSlotPageResponse(responseID);
                break;
            case "SelectCategoryPage":
                HandleSelectCategoryPage(responseID);
                break;
            case "SelectAbilityPage":
                HandleSelectAbilityPage(responseID);
                break;
            case "ConfirmAbilityPage":
                HandleConfirmAbilityPage(responseID);
                break;
        }
    }

    private AdjustAbilitiesViewModel GetModel()
    {
        return (AdjustAbilitiesViewModel)GetDialogCustomData();
    }

    private void LoadSlotPageResponses()
    {
        AdjustAbilitiesViewModel model = GetModel();
        DialogPage page = GetPageByName("SelectSlotPage");
        page.getResponses().clear();

        for(int slot = 1; slot <= model.getNumberOfAbilitySlots(); slot++)
        {
            if(slot == 1)
            {
                if(model.getEquippedAbilities().getSlot1() == null)
                    page.addResponse("Slot #1: (EMPTY)", true);
                else page.addResponse("Slot #1: " + model.getEquippedAbilities().getSlot1().getName(), true);
            }
            else if(slot == 2)
            {
                if(model.getEquippedAbilities().getSlot2() == null)
                    page.addResponse("Slot #2: (EMPTY)", true);
                else page.addResponse("Slot #2: " + model.getEquippedAbilities().getSlot2().getName(), true);
            }
            else if(slot == 3)
            {
                if(model.getEquippedAbilities().getSlot3() == null)
                    page.addResponse("Slot #3: (EMPTY)", true);
                else page.addResponse("Slot #3: " + model.getEquippedAbilities().getSlot3().getName(), true);
            }
            else if(slot == 4)
            {
                if(model.getEquippedAbilities().getSlot4() == null)
                    page.addResponse("Slot #4: (EMPTY)", true);
                else page.addResponse("Slot #4: " + model.getEquippedAbilities().getSlot4().getName(), true);
            }
            else if(slot == 5)
            {
                if(model.getEquippedAbilities().getSlot5() == null)
                    page.addResponse("Slot #5: (EMPTY)", true);
                else page.addResponse("Slot #5: " + model.getEquippedAbilities().getSlot5().getName(), true);
            }
            else if(slot == 6)
            {
                if(model.getEquippedAbilities().getSlot6() == null)
                    page.addResponse("Slot #6: (EMPTY)", true);
                else page.addResponse("Slot #6: " + model.getEquippedAbilities().getSlot6().getName(), true);
            }
            else if(slot == 7)
            {
                if(model.getEquippedAbilities().getSlot7() == null)
                    page.addResponse("Slot #7: (EMPTY)", true);
                else page.addResponse("Slot #7: " + model.getEquippedAbilities().getSlot7().getName(), true);
            }
            else if(slot == 8)
            {
                if(model.getEquippedAbilities().getSlot8() == null)
                    page.addResponse("Slot #8: (EMPTY)", true);
                else page.addResponse("Slot #8: " + model.getEquippedAbilities().getSlot8().getName(), true);
            }
            else if(slot == 9)
            {
                if(model.getEquippedAbilities().getSlot9() == null)
                    page.addResponse("Slot #9: (EMPTY)", true);
                else page.addResponse("Slot #9: " + model.getEquippedAbilities().getSlot9().getName(), true);
            }
            else if(slot == 10)
            {
                if(model.getEquippedAbilities().getSlot10() == null)
                    page.addResponse("Slot #10: (EMPTY)", true);
                else page.addResponse("Slot #10: " + model.getEquippedAbilities().getSlot10().getName(), true);
            }
        }

        page.addResponse("Back", true);

    }

    private void LoadCategoryPageResponses()
    {
        AdjustAbilitiesViewModel model = GetModel();
        DialogPage page = GetPageByName("SelectCategoryPage");

        for(AbilityCategoryEntity category : model.getCategories())
        {
            page.addResponse(category.getName(), true, category.getAbilityCategoryID());
        }

        page.addResponse("Back", true, -1);
    }

    private void LoadAbilityPageResponses(int categoryID)
    {
        NWObject pc = GetPC();
        PlayerGO pcGO = new PlayerGO(pc);
        MagicRepository repo = new MagicRepository();
        DialogPage page = GetPageByName("SelectAbilityPage");
        List<PCLearnedAbilityEntity> abilities = repo.GetPCLearnedAbilitiesByCategoryID(pcGO.getUUID(), categoryID);
        page.getResponses().clear();

        for(PCLearnedAbilityEntity ability : abilities)
        {
            page.addResponse("Equip: " + ability.getAbility().getName(), true, ability.getAbility().getAbilityID());
        }

        page.addResponse("Back", true, -1);
    }

    private void HandleMainPageResponse(int responseID)
    {
        AdjustAbilitiesViewModel model = GetModel();
        switch(responseID)
        {
            case 1: // Equip Abilities
                model.setEquipping(true);
                ChangePage("SelectSlotPage");
                break;
            case 2: // Unequip Abilities
                model.setEquipping(false);
                ChangePage("SelectSlotPage");
                break;
        }

        SetDialogCustomData(model);
    }

    private void HandleSelectSlotPageResponse(int responseID)
    {
        NWObject pc = GetPC();
        String responseText = GetResponseByID("SelectSlotPage", responseID).getText();

        if(Objects.equals(responseText, "Back"))
        {
            ChangePage("MainPage");
            return;
        }

        AdjustAbilitiesViewModel model = GetModel();

        // Equipping Ability
        if(model.isEquipping())
        {
            model.setCurrentSlot(responseID);
            ChangePage("SelectCategoryPage");
        }
        // Unequipping Ability
        else
        {
            PCEquippedAbilityEntity entity = MagicSystem.UnequipAbility(pc, responseID);
            model.setEquippedAbilities(entity);
            LoadSlotPageResponses();
        }
    }

    private void HandleSelectCategoryPage(int responseID)
    {
        DialogResponse response = GetResponseByID("SelectCategoryPage", responseID);
        String responseText = response.getText();

        if(Objects.equals(responseText, "Back"))
        {
            ChangePage("SelectSlotPage");
            return;
        }

        int categoryID = (int)response.getCustomData();
        LoadAbilityPageResponses(categoryID);
        ChangePage("SelectAbilityPage");
    }

    private void HandleSelectAbilityPage(int responseID)
    {
        AdjustAbilitiesViewModel model = GetModel();
        DialogResponse response = GetResponseByID("SelectAbilityPage", responseID);
        String responseText = response.getText();

        if(Objects.equals(responseText, "Back"))
        {
            ChangePage("SelectCategoryPage");
            return;
        }

        int abilityID = (int)response.getCustomData();
        model.setSelectedAbilityID(abilityID);

        SetPageHeader("ConfirmAbilityPage", BuildAbilityDetailsHeader(abilityID));
        ChangePage("ConfirmAbilityPage");
    }

    private String BuildAbilityDetailsHeader(int abilityID)
    {
        MagicRepository repo = new MagicRepository();
        AbilityEntity entity = repo.GetAbilityByID(abilityID);
        String header = ColorToken.Green() + "Ability: " + ColorToken.End() + entity.getName() + "\n\n";
        header += ColorToken.Green() + "Description: " + ColorToken.End() + entity.getDescription() + "\n\n";

        if(entity.getBaseManaCost() > 0) {
            header += ColorToken.Green() + "Mana Cost: " + ColorToken.End() + entity.getBaseManaCost();
        }

        return header;
    }

    private void HandleConfirmAbilityPage(int responseID)
    {
        switch(responseID)
        {
            case 1: // Equip ability

                AdjustAbilitiesViewModel model = GetModel();
                int abilityID = model.getSelectedAbilityID();
                PCEquippedAbilityEntity entity = model.getEquippedAbilities();
                if(   (entity.getSlot1() != null && entity.getSlot1().getAbilityID() == abilityID) ||
                        (entity.getSlot2() != null && entity.getSlot2().getAbilityID() == abilityID) ||
                        (entity.getSlot3() != null && entity.getSlot3().getAbilityID() == abilityID) ||
                        (entity.getSlot4() != null && entity.getSlot4().getAbilityID() == abilityID) ||
                        (entity.getSlot5() != null && entity.getSlot5().getAbilityID() == abilityID) ||
                        (entity.getSlot6() != null && entity.getSlot6().getAbilityID() == abilityID) ||
                        (entity.getSlot7() != null && entity.getSlot7().getAbilityID() == abilityID) ||
                        (entity.getSlot8() != null && entity.getSlot8().getAbilityID() == abilityID) ||
                        (entity.getSlot9() != null && entity.getSlot9().getAbilityID() == abilityID) ||
                        (entity.getSlot10() != null && entity.getSlot10().getAbilityID() == abilityID))
                {
                    NWScript.floatingTextStringOnCreature(ColorToken.Red() + "That ability is already equipped." + ColorToken.End(), GetPC(), false);
                    return;
                }


                entity = MagicSystem.EquipAbility(GetPC(), model.getCurrentSlot(), abilityID);
                model.setEquippedAbilities(entity);
                LoadSlotPageResponses();

                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEquippingAbilities) == 1)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampEquippingAbilities);
                }

                ChangePage("SelectSlotPage");
                break;
            case 2: // Back
                ChangePage("SelectAbilityPage");
                break;
        }

    }

    @Override
    public void EndDialog() {

    }
}
