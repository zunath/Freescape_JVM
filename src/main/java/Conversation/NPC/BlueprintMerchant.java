package Conversation.NPC;

import Dialog.*;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class BlueprintMerchant extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Our supplies have run low so we've resorted to building most of the things we need. Please take a look at my collection of blueprints and let me know if you'd like to buy anything.",
                "I have questions...",
                "Buy Engineering Blueprints",
                "Buy Smithery Blueprints",
                "Buy Mixing Blueprints",
                "Buy/Sell General Items"
        );

        DialogPage questionsPage = new DialogPage(
                "What would you like to ask me?",
                "How does crafting work?",
                "Why are you charging money?",
                "Where can I find a workbench?",
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
                HandleMainPageSelection(responseID);
                break;
            case "QuestionsPage":
                HandleQuestionsPage(responseID);
                break;
        }

    }

    private void HandleMainPageSelection(int responseID)
    {
        NWObject oPC = GetPC();
        NWObject store;
        switch(responseID)
        {
            case 1: // Questions
                ChangePage("QuestionsPage");
                break;
            case 2: // Engineering Blueprints
                store = NWScript.getNearestObjectByTag("mch_blueprints1", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 3: // Smithery Blueprints
                store = NWScript.getNearestObjectByTag("mch_blueprints2", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 4: // Mixing Blueprints
                store = NWScript.getNearestObjectByTag("mch_blueprints3", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
            case 5: // Buy/Sell General Items
                store = NWScript.getNearestObjectByTag("general_items", oPC, 1);
                NWScript.openStore(store, oPC, 0, 0);
                DialogManager.endConversation(oPC);
                break;
        }
    }

    private void HandleQuestionsPage(int responseID)
    {
        switch (responseID)
        {
            case 1: // How does crafting work?
                String header = "Crafting is a relatively simple process. Just follow my instructions:\n\n" +
                        "1.) Purchase a blueprint from me.\n" +
                        "2.) Open your inventory and use the blueprint to add it to your collection.\n" +
                        "3.) Locate the appropriate workbench for your craft. If you can't find one, keep in mind you can build it yourself using the 'Structure Tool' on your Omni Tool.\n" +
                        "4.) Open the workbench and select 'Choose Blueprint'.\n" +
                        "5.) Select the blueprint of the item you would like to make.\n" +
                        "6.) Put the items required for crafting into the workbench.\n" +
                        "7.) Select the 'Craft' option.\n\n" +
                        "It looks complicated but once you get the hang of it, it's pretty simple!";
                SetPageHeader("QuestionsPage", header);
                break;
            case 2: // Why are you charging money?
                SetPageHeader("QuestionsPage", "Believe it or not, money still has use even in the end of the world. Many of the other outposts will accept it in exchange for goods.");
                break;
            case 3: // Where can I find a workbench?
                SetPageHeader("QuestionsPage", "Look around nearby - another survivor may have built a workbench. But if you don't have luck there you can always build one yourself. Simply use your Omni Tool's 'Structure Tool' to build a territory marker. After that you can build a workbench by using the Structure Tool near the flag.");
                break;
            case 4: // Back
                SetPageHeader("QuestionsPage", "What would you like to ask me?");
                ChangePage("MainPage");
                break;
        }
    }


    @Override
    public void EndDialog() {

    }
}
