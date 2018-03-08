package Conversation.Quest;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Enumerations.QuestID;
import GameSystems.QuestSystem;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("unused")
public class Tutorial extends DialogBase implements IDialogHandler {

    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Well met, rookie. If you plan to survive this chaos you best listen to me.",
                "I need training.",
                "What is your role here?"
        );

        DialogPage rolePage = new DialogPage(
                "I serve as the outpost's survival expert. Scrubs like you have to be trained to survive on their own out in the wasteland. If you don't want to die, you better do exactly what I say. Think you know more than me? Then get out of my face and go prove yourself!",
                "Back"
        );

        DialogPage trainingPage = new DialogPage(
                "What kind of training do you need, scrub?",
                "[QUEST] Leveling Up",
                "[QUEST] Equipping Abilities",
                "[QUEST] Key Items",
                "[QUEST] Searching",
                "[QUEST] Building Structures",
                "[QUEST] Eating",
                "Back"
        );

        DialogPage levelingUpPage = new DialogPage(
                "Ready to upgrade your skills, scrub? Open up your skill allocation menu in your Omni Tool and start making upgrades. You can do this by pressing 'R' or actively using the Omni Tool in your inventory. When you've figured this much out, come back and let me know.",
                "Understood. I will allocate my skill points and return back to you.",
                "Back"
        );
        DialogPage levelingUpInProgressPage = new DialogPage(
                "Have you accessed your skill allocation menu yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage equippingAbilitiesPage = new DialogPage(
                "Ready to equip an ability, scrub? Learn an ability by using an Ability Disc. Then, equip it by using an AMP terminal.",
                "Understood. I will learn an ability by using an Ability Disc and then equip it by using the AMP terminal nearby.",
                "Back"
        );
        DialogPage equippingAbilitiesInProgressPage = new DialogPage(
                "Have you equipped an ability yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage keyItemsPage = new DialogPage(
                "Key items are important things, scrub. Your Omni Tool will hold them for you so you don't accidentally lose them. Access your key items by pressing 'R' or actively using the Omni Tool in your inventory. When you've figured this much out, come back and let me know.",
                "Understood. I will access my key items and return back to you.",
                "Back"
        );
        DialogPage keyItemsInProgressPage = new DialogPage(
                "Have you accessed your key items yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage searchingPage = new DialogPage(
                "Searching is vital to your survival, scrub. For this mission I want you to leave the outpost and look for a place worth searching. When you've completed this task return to me for your reward.",
                "Understood. I will look for items at a search site and return back to you.",
                "Back"
        );
        DialogPage searchingInProgressPage = new DialogPage(
                "Have you searched for items yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage buildingStructuresPage = new DialogPage(
                "You will need to be able to live off the land yourself, scrub. You can't count on us to protect you forever. For this task, I want you to build a construction site by using the Omni Tool in your inventory. When this is done, return to me.",
                "Understood. I will create a construction site by using my Omni Tool and return back to you.",
                "Back"
        );
        DialogPage buildingStructuresInProgressPage = new DialogPage(
                "Have you built a construction site yet, scrub?",
                "Not yet...",
                "Back"
        );

        DialogPage eatingPage = new DialogPage(
                "Scrub, we don't have the luxury of technology to keep us fed anymore. You need to take care of it yourself. Get some food, consume it, and get back to me. Gods help me if you don't know how to do that!",
                "Understood. I will consume food and return back to you.",
                "Back"
        );

        DialogPage eatingInProgressPage = new DialogPage(
                "I'm glad you haven't starved yet, scrub. You better eat some food and get back to me.",
                "I'll eat some food and get back to you.",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("RolePage", rolePage);
        dialog.addPage("TrainingPage", trainingPage);
        dialog.addPage("LevelingUpPage", levelingUpPage);
        dialog.addPage("EquippingAbilitiesPage", equippingAbilitiesPage);
        dialog.addPage("KeyItemsPage", keyItemsPage);
        dialog.addPage("SearchingPage", searchingPage);
        dialog.addPage("BuildingStructuresPage", buildingStructuresPage);
        dialog.addPage("EatingPage", eatingPage);

        dialog.addPage("LevelingUpInProgressPage", levelingUpInProgressPage);
        dialog.addPage("EquippingAbilitiesInProgressPage", equippingAbilitiesInProgressPage);
        dialog.addPage("KeyItemsInProgressPage", keyItemsInProgressPage);
        dialog.addPage("SearchingInProgressPage", searchingInProgressPage);
        dialog.addPage("BuildingStructuresInProgressPage", buildingStructuresInProgressPage);
        dialog.addPage("EatingInProgressPage", eatingInProgressPage);

        return dialog;
    }

    @Override
    public void Initialize() {
        RefreshConversationOptions();
    }

    private void RefreshConversationOptions()
    {
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampLevelingUp)) // Leveling Up
            GetResponseByID("TrainingPage", 1).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampEquippingAbilities)) // Equipping Abilities
            GetResponseByID("TrainingPage", 2).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampKeyItems)) // Key Items
            GetResponseByID("TrainingPage", 3).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampSearching)) // Searching
            GetResponseByID("TrainingPage", 4).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampBuildingStructures)) // Building Structures
            GetResponseByID("TrainingPage", 5).setActive(false);
        if(QuestSystem.HasPlayerCompletedQuest(GetPC(), QuestID.BootCampEating)) // Eating
            GetResponseByID("TrainingPage", 6).setActive(false);

        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampLevelingUp) == 2)
            GetResponseByID("LevelingUpInProgressPage", 1).setText("I have accessed my skill allocation menu.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEquippingAbilities) == 2)
            GetResponseByID("EquippingAbilitiesInProgressPage", 1).setText("I have equipped an ability.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampKeyItems) == 2)
            GetResponseByID("KeyItemsInProgressPage", 1).setText("I have accessed my key items.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampSearching) == 2)
            GetResponseByID("SearchingInProgressPage", 1).setText("I have finished searching a location.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampBuildingStructures) == 2)
            GetResponseByID("BuildingStructuresInProgressPage", 1).setText("I have built a construction site.");
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEating) == 2)
            GetResponseByID("EatingInProgressPage", 1).setText("I have eaten food.");
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageSelection(responseID);
                break;
            case "RolePage":
                HandleRolePageSelection(responseID);
                break;
            case "TrainingPage":
                HandleTrainingPageSelection(responseID);
                break;
            case "LevelingUpPage":
                HandleLevelingUpPageSelection(responseID);
                break;
            case "LevelingUpInProgressPage":
                HandleLevelingUpInProgressPageSelection(responseID);
                break;
            case "EquippingAbilitiesPage":
                HandleEquippingAbilitiesPageSelection(responseID);
                break;
            case "EquippingAbilitiesInProgressPage":
                HandleEquippingAbilitiesInProgressPageSelection(responseID);
                break;
            case "KeyItemsPage":
                HandleKeyItemsPageSelection(responseID);
                break;
            case "KeyItemsInProgressPage":
                HandleKeyItemsPageInProgressSelection(responseID);
                break;
            case "SearchingPage":
                HandleSearchingPageSelection(responseID);
                break;
            case "SearchingInProgressPage":
                HandleSearchingPageInProgressSelection(responseID);
                break;
            case "BuildingStructuresPage":
                HandleBuildingStructuresPageSelection(responseID);
                break;
            case "BuildingStructuresInProgressPage":
                HandleBuildingStructuresInProgressPageSelection(responseID);
                break;
            case "EatingPage":
                HandleEatingPageSelection(responseID);
                break;
            case "EatingInProgressPage":
                HandleEatingInProgressPageSelection(responseID);
                break;
        }
    }

    private void HandleMainPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1: // I need training
                ChangePage("TrainingPage");
                break;
            case 2: // What is your role here?
                ChangePage("RolePage");
                break;
        }
    }

    private void HandleRolePageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleTrainingPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1: // Leveling Up
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampLevelingUp) == -1)
                    ChangePage("LevelingUpPage");
                else ChangePage("LevelingUpInProgressPage");
                break;
            case 2: // Equipping Abilities
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEquippingAbilities) == -1)
                    ChangePage("EquippingAbilitiesPage");
                else ChangePage("EquippingAbilitiesInProgressPage");
                break;
            case 3: // Key Items
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampKeyItems) == -1)
                    ChangePage("KeyItemsPage");
                else ChangePage("KeyItemsInProgressPage");
                break;
            case 4: // Searching
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampSearching) == -1)
                    ChangePage("SearchingPage");
                else ChangePage("SearchingInProgressPage");
                break;
            case 5: // Building Structures
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampBuildingStructures) == -1)
                    ChangePage("BuildingStructuresPage");
                else ChangePage("BuildingStructuresInProgressPage");
                break;
            case 6: // Eating
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEating) == -1)
                    ChangePage("EatingPage");
                else ChangePage("EatingInProgressPage");
                break;
            case 7: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleLevelingUpPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampLevelingUp);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleLevelingUpInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampLevelingUp) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampLevelingUp);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleEquippingAbilitiesPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampEquippingAbilities);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleEquippingAbilitiesInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEquippingAbilities) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampEquippingAbilities);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleKeyItemsPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampKeyItems);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleKeyItemsPageInProgressSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampKeyItems) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampKeyItems);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleSearchingPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampSearching);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleSearchingPageInProgressSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampSearching) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampSearching);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleBuildingStructuresPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampBuildingStructures);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleBuildingStructuresInProgressPageSelection(int responseID)
    {
        switch (responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampBuildingStructures) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampBuildingStructures);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleEatingPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1:
                QuestSystem.AcceptQuest(GetPC(), QuestID.BootCampEating);
                EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    private void HandleEatingInProgressPageSelection(int responseID)
    {
        switch(responseID)
        {
            case 1:
                if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampEating) == 2)
                {
                    QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampEating);
                    RefreshConversationOptions();
                    ChangePage("MainPage");
                }
                else EndConversation();
                break;
            case 2:
                ChangePage("TrainingPage");
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
