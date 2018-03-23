package Conversation;

import Data.Repository.KeyItemRepository;
import Dialog.*;
import Entities.KeyItemEntity;
import Entities.PCKeyItemEntity;
import GameObject.PlayerGO;
import Helper.ColorToken;
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
                "Maps",
                "Quest Items",
                "Documents",
                "Back"
        );

        DialogPage keyItemListPage = new DialogPage(
                "Select a key item."
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("KeyItemsListPage", keyItemListPage);
        return dialog;
    }

    @Override
    public void Initialize()
    {
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {

        switch (pageName)
        {
            case "MainPage":
                switch (responseID) {
                    case 1: // "Maps"
                    case 2: // "Quest Items"
                    case 3: // "Documents"
                        NWScript.setLocalInt(GetPC(), "TEMP_MENU_KEY_ITEM_CATEGORY_ID", responseID);
                        LoadKeyItemsOptions(responseID);
                        break;
                    case 4: // "Back"
                        SwitchConversation("RestMenu");
                        break;
                }
                break;
            case "KeyItemsListPage":
                HandleKeyItemSelectionr(responseID);
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

}
