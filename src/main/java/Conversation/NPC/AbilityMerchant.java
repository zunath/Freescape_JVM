package Conversation.NPC;

import Dialog.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class AbilityMerchant extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "My scanner detects that you've been implanted with an AMP. I've got ability discs you can install if you would like to take a look. Which would you like to see?",
                "I have some questions first.",
                "Buy Evocation Magic",
                "Buy Enhancing Magic",
                "Buy Holy Magic",
                "Buy Passive Abilities",
                "Buy Crafting Abilities",
                "Buy/Sell General Items"
        );

        DialogPage questionsPage = new DialogPage(
                "What would you like to ask me?",
                "How do I learn new abilities?",
                "How do I cast spells?",
                "What is mana?",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("QuestionsPage", questionsPage);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {

        switch(pageName)
        {
            case "MainPage":
                HandleMainPageActions(responseID);
                break;
            case "QuestionsPage":
                HandleQuestionsPageActions(responseID);
                break;
        }
    }

    private void HandleMainPageActions(int responseID)
    {
        NWObject oPC = GetPC();
        NWObject store;
        switch (responseID)
        {
            case 1: // I have questions
                ChangePage("QuestionsPage");
                break;
            case 2: // Evocation Magic
                store = NWScript.getNearestObjectByTag("abil_store1", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 3: // Enhancing Magic
                store = NWScript.getNearestObjectByTag("abil_store2", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 4: // Holy Magic
                store = NWScript.getNearestObjectByTag("abil_store3", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 5: // Passive Abilities
                store = NWScript.getNearestObjectByTag("abil_store4", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 6: // Crafting Abilities
                store = NWScript.getNearestObjectByTag("abil_store5", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 7: // Buy/Sell General Items
                store = NWScript.getNearestObjectByTag("general_items", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
        }
    }

    private void HandleQuestionsPageActions(int responseID)
    {
        switch(responseID)
        {
            case 1: // How do I learn new abilities?
                SetPageHeader("QuestionsPage", "Simply purchase an ability disc from me. Then, open your inventory and use the item to install it into your AMP. Once that's done you can equip the ability using the AMP terminal next to me. \n\nMake sure you have purchased an ability slot from the 'Rest Menu'.");
                break;
            case 2: // How do I cast spells?
                String header = "Casting spells requires the following: \n\n" +
                        "1.) An installed ability. You can install abilities by purchasing the disc from me and then use it from your inventory.\n" +
                        "2.) An open ability slot. If you don't have one, you can purchase it with Skill Points (SP) via your 'Rest Menu' (press R).\n" +
                        "3.) Sufficient mana. Your maximum mana can be increased by purchasing upgrades with Skill Points (SP) via your 'Rest Menu' (press R).\n\n" +
                        "Once you have satisfied the above requirements you can equip the ability using the AMP terminal next to me. After that you will find the ability on your Radial Menu.";
                SetPageHeader("QuestionsPage", header);
                break;
            case 3: // What is Mana?
                SetPageHeader("QuestionsPage", "Mana is the resource by which you can activate abilities such as spells. Mana regenerates naturally over time and can be recovered by using the appropriate first aid kit or similar item. Finally, you can increase your maximum mana pool by purchasing upgrades with Skill Points (SP) via the rest menu (Press 'R').");
                break;
            case 4:
                SetPageHeader("QuestionsPage", "What would you like to ask me?");
                ChangePage("MainPage");
                break;
        }
    }

    @Override
    public void EndDialog() {

    }
}
