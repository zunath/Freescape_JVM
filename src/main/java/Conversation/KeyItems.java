package Conversation;

import Dialog.*;
import Entities.CraftBlueprintCategoryEntity;
import Entities.KeyItemEntity;
import Entities.PCBlueprintEntity;
import Entities.PCKeyItemEntity;
import Enumerations.QuestID;
import GameObject.PlayerGO;
import GameSystems.QuestSystem;
import Helper.ColorToken;
import Data.Repository.CraftRepository;
import Data.Repository.KeyItemRepository;
import GameSystems.CraftSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class KeyItems extends DialogBase implements IDialogHandler {

    @Override
    public PlayerDialog SetUp(NWObject oPC) {

        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Select a key item category.",
                "Blueprints",
                "Maps",
                "Quest Items",
                "Documents",
                "Back"
        );

        DialogPage keyItemListPage = new DialogPage(
                "Select a key item."
        );

        DialogPage blueprintCategoryPage = new DialogPage(
                "Select a blueprint category."
        );

        DialogPage blueprintListPage = new DialogPage(
                "Select a blueprint."
        );

        DialogPage blueprintPage = new DialogPage(
                "<SET LATER>",
                "Back"
        );


        dialog.addPage("MainPage", mainPage);
        dialog.addPage("KeyItemsListPage", keyItemListPage);
        dialog.addPage("BlueprintCategoryPage", blueprintCategoryPage);
        dialog.addPage("BlueprintListPage", blueprintListPage);
        dialog.addPage("BlueprintPage", blueprintPage);
        return dialog;
    }

    @Override
    public void Initialize()
    {
        if(QuestSystem.GetPlayerQuestJournalID(GetPC(), QuestID.BootCampKeyItems) == 1)
        {
            QuestSystem.AdvanceQuestState(GetPC(), QuestID.BootCampKeyItems);
        }
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {

        switch (pageName)
        {
            case "MainPage":
                switch (responseID) {
                    case 1: // "Blueprints"
                        LoadBlueprintCategoryPage();
                        ChangePage("BlueprintCategoryPage");
                        break;
                    case 2: // "Maps"
                    case 3: // "Quest Items"
                    case 4: // "Documents"
                        NWScript.setLocalInt(GetPC(), "TEMP_MENU_KEY_ITEM_CATEGORY_ID", responseID);
                        LoadKeyItemsOptions(responseID);
                        break;
                    case 5: // "Back"
                        SwitchConversation("RestMenu");
                        break;
                }
                break;
            case "KeyItemsListPage":
                HandleKeyItemSelectionr(responseID);
                break;
            case "BlueprintCategoryPage":
                HandleBlueprintCategoryResponse(responseID);
                break;
            case "BlueprintListPage":
                HandleBlueprintListResponse(responseID);
                break;
            case "BlueprintPage":
                HandleBlueprintResponse(responseID);
                break;
        }
    }

    @Override
    public void EndDialog() {
        ClearTempVariables();
    }


    private void ClearTempVariables()
    {
        NWScript.deleteLocalString(GetPC(), "TEMP_MENU_KEY_ITEM_CATEGORY_ID");
        SetPageHeader("KeyItemsListPage", "Select a key item.");
    }

    private void LoadKeyItemsOptions(int categoryID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        PlayerDialog dialog = DialogManager.loadPlayerDialog(pcGO.getUUID());
        DialogPage page = dialog.getPageByName("KeyItemsListPage");
        page.getResponses().clear();
        KeyItemRepository repo = new KeyItemRepository();
        List<PCKeyItemEntity> items = repo.GetPlayerKeyItemsByCategory(pcGO.getUUID(), categoryID);

        for(PCKeyItemEntity item : items)
        {
            DialogResponse response = new DialogResponse(item.getKeyItem().getName());
            response.setCustomData(item.getKeyItemID());
            page.getResponses().add(response);
        }

        page.getResponses().add(new DialogResponse("Back"));
        ChangePage("KeyItemsListPage");
    }

    private void HandleKeyItemSelectionr(int responseID)
    {
        DialogResponse response = GetResponseByID(GetCurrentPageName(), responseID);
        if(response.getCustomData() == null)
        {
            ClearTempVariables();
            ChangePage("MainPage");
        }
        else
        {
            SetPageHeader("KeyItemsListPage", BuildKeyItemHeader(responseID));
        }
    }

    private String BuildKeyItemHeader(int responseID)
    {
        KeyItemRepository repo = new KeyItemRepository();
        DialogResponse response = GetResponseByID(GetCurrentPageName(), responseID);
        int keyItemID = (int)response.getCustomData();
        KeyItemEntity entity = repo.GetKeyItemByID(keyItemID);

        String header = ColorToken.Green() + "Key Item: " + ColorToken.End() + entity.getName() + "\n\n";
        header += ColorToken.Green() + "Description: " + ColorToken.End() + entity.getDescription() + "\n";

        return header;
    }


    private void LoadBlueprintCategoryPage()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        CraftRepository repo = new CraftRepository();
        List<CraftBlueprintCategoryEntity> categories = repo.GetCategoriesAvailableToPC(pcGO.getUUID());
        DialogPage page = GetPageByName("BlueprintCategoryPage");
        page.getResponses().clear();

        for(CraftBlueprintCategoryEntity category : categories)
        {
            page.addResponse(category.getName(), true, category.getCraftBlueprintCategoryID());
        }

        page.addResponse("Back", true);
    }

    private void HandleBlueprintCategoryResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintCategoryPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("MainPage");
            return;
        }
        int categoryID = (int)response.getCustomData();
        LoadBlueprintListPage(categoryID);
        ChangePage("BlueprintListPage");
    }

    private void LoadBlueprintListPage(int categoryID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        CraftRepository repo = new CraftRepository();
        List<PCBlueprintEntity> blueprints = repo.GetPCBlueprintsByCategoryID(pcGO.getUUID(), categoryID);
        DialogPage page = GetPageByName("BlueprintListPage");
        page.getResponses().clear();

        for(PCBlueprintEntity bp : blueprints)
        {
            page.addResponse(bp.getBlueprint().getItemName(), true, bp.getBlueprint().getCraftBlueprintID());
        }

        page.addResponse("Back", true);
    }

    private void HandleBlueprintListResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintListPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("BlueprintCategoryPage");
            return;
        }
        PlayerGO pcGO = new PlayerGO(GetPC());
        int blueprintID = (int)response.getCustomData();

        SetPageHeader("BlueprintPage", CraftSystem.BuildBlueprintHeader(GetPC(), blueprintID));
        ChangePage("BlueprintPage");
    }

    private void HandleBlueprintResponse(int responseID)
    {
        if(responseID == 1)
        {
            ChangePage("BlueprintListPage");
        }
    }
}
