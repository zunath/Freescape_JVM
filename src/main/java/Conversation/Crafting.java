package Conversation;

import Dialog.*;
import Entities.*;
import GameObject.PlayerGO;
import Data.Repository.CraftRepository;
import GameSystems.CraftSystem;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import java.util.List;

@SuppressWarnings("unused")
public class Crafting extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select a blueprint. Only blueprints you've added to your collection will be displayed here.",
                "Back"
        );
        DialogPage blueprintPage = new DialogPage(
                "<SET LATER>",
                ColorToken.Green() + "Examine Item" + ColorToken.End(),
                "Select Blueprint",
                "Back"
        );
        DialogPage blueprintListPage = new DialogPage(
                "Please select a blueprint. Only blueprints you've added to your collection will be displayed here.",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("BlueprintListPage", blueprintListPage);
        dialog.addPage("BlueprintPage", blueprintPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        LoadCategoryResponses();
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleCategoryResponse(responseID);
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

    }

    private void LoadCategoryResponses()
    {
        int craftID = NWScript.getLocalInt(GetDialogTarget(), "CRAFT_ID");
        PlayerGO pcGO = new PlayerGO(GetPC());
        CraftRepository repo = new CraftRepository();
        List<CraftBlueprintCategoryEntity> categories = repo.GetCategoriesAvailableToPCByCraftID(pcGO.getUUID(), craftID);
        DialogPage page = GetPageByName("MainPage");
        page.getResponses().clear();

        for(CraftBlueprintCategoryEntity category : categories)
        {
            page.addResponse(category.getName(), category.isActive(), category.getCraftBlueprintCategoryID());
        }

        page.addResponse("Back", true);
    }

    private void LoadBlueprintListPage(int categoryID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        CraftRepository repo = new CraftRepository();
        int craftID = NWScript.getLocalInt(GetDialogTarget(), "CRAFT_ID");

        List<PCBlueprintEntity> blueprints = repo.GetPCBlueprintsForCraftByCategoryID(pcGO.getUUID(), craftID, categoryID);
        DialogPage page = GetPageByName("BlueprintListPage");
        page.getResponses().clear();

        for(PCBlueprintEntity bp : blueprints)
        {
            page.addResponse(bp.getBlueprint().getItemName(), true, bp.getBlueprint().getCraftBlueprintID());
        }

        page.addResponse("Back", true);
    }

    private void LoadBlueprintPage(int blueprintID)
    {
        DialogPage page = GetPageByName("BlueprintPage");
        SetPageHeader("BlueprintPage", CraftSystem.BuildBlueprintHeader(GetPC(), blueprintID));
        GetResponseByID("BlueprintPage", 1).setCustomData(blueprintID);
        GetResponseByID("BlueprintPage", 2).setCustomData(blueprintID);
    }

    private void HandleCategoryResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("MainPage", responseID);
        if(response.getCustomData() == null)
        {
            final NWObject device = GetDialogTarget();
            Scheduler.assign(GetPC(), () -> NWScript.actionInteractObject(device));
            return;
        }

        int categoryID = (int)response.getCustomData();
        LoadBlueprintListPage(categoryID);
        ChangePage("BlueprintListPage");
    }

    private void HandleBlueprintListResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintListPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("MainPage");
            return;
        }

        int blueprintID = (int)response.getCustomData();
        LoadBlueprintPage(blueprintID);
        ChangePage("BlueprintPage");
    }

    private void HandleBlueprintResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintPage", responseID);
        int blueprintID;

        switch (responseID)
        {
            case 1: // Examine item
                blueprintID = (int)response.getCustomData();
                CraftRepository repo = new CraftRepository();
                CraftBlueprintEntity entity = repo.GetBlueprintByID(blueprintID);
                NWObject tempContainer = NWScript.getObjectByTag("craft_temp_storage", 0);
                final NWObject examineItem = NWScript.createItemOnObject(entity.getItemResref(), tempContainer, 1, "");
                NWScript.destroyObject(examineItem, 0.1f);

                Scheduler.assign(GetPC(), () -> NWScript.actionExamine(examineItem));

                break;
            case 2: // Select Blueprint
                blueprintID = (int)response.getCustomData();
                NWScript.sendMessageToPC(GetPC(), "Blueprint selected. Add necessary resources to the device and click the 'Craft Item' option.");

                final NWObject device = GetDialogTarget();
                NWScript.setLocalInt(device, "CRAFT_BLUEPRINT_ID", blueprintID);

                Scheduler.assign(GetPC(), () -> NWScript.actionInteractObject(device));

                EndConversation();
                break;
            case 3: // Back
                ChangePage("BlueprintListPage");
                break;
        }
    }

}
