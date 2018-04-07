package Conversation;

import Conversation.ViewModels.ViewBlueprintsViewModel;
import Data.Repository.CraftRepository;
import Data.Repository.StructureRepository;
import Dialog.*;
import Entities.CraftBlueprintCategoryEntity;
import Entities.CraftBlueprintEntity;
import Entities.StructureBlueprintEntity;
import Entities.StructureCategoryEntity;
import GameObject.PlayerGO;
import GameSystems.CraftSystem;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class ViewBlueprints extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");

        DialogPage mainPage = new DialogPage(
                "Which blueprints would you like to view?",
                "Crafting Blueprints",
                "Construction Blueprints",
                "Back"
        );

        DialogPage craftCategoriesPage = new DialogPage(
                "Which category would you like to view?"
        );

        DialogPage constructionCategoriesPage = new DialogPage(
                "Which category would you like to view?"
        );

        DialogPage blueprintList = new DialogPage(
                "Which blueprint would you like to view?"
        );

        DialogPage blueprintDetails = new DialogPage(
                "<SET LATER>",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("CraftCategoriesPage", craftCategoriesPage);
        dialog.addPage("ConstructionCategoriesPage", constructionCategoriesPage);
        dialog.addPage("BlueprintListPage", blueprintList);
        dialog.addPage("BlueprintDetailsPage", blueprintDetails);

        return dialog;
    }

    @Override
    public void Initialize() {
        PlayerGO pcGO = new PlayerGO(GetPC());
        ViewBlueprintsViewModel vm = new ViewBlueprintsViewModel();
        CraftRepository craftRepo = new CraftRepository();
        StructureRepository structureRepo = new StructureRepository();

        vm.setCraftCategories(craftRepo.GetCategoriesAvailableToPC(pcGO.getUUID()));
        vm.setStructureCategories(structureRepo.GetStructureCategories(pcGO.getUUID()));

        for(CraftBlueprintCategoryEntity category: vm.getCraftCategories())
        {
            AddResponseToPage("CraftCategoriesPage", category.getName(), true, category.getCraftBlueprintCategoryID());
        }
        AddResponseToPage("CraftCategoriesPage", "Back", true, -1);

        for(StructureCategoryEntity category: vm.getStructureCategories())
        {
            AddResponseToPage("ConstructionCategoriesPage", category.getName(), true, category.getStructureCategoryID());
        }
        AddResponseToPage("ConstructionCategoriesPage", "Back", true, -1);

        SetDialogCustomData(vm);
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "CraftCategoriesPage":
                HandleCraftCategoriesPageResponse(responseID);
                break;
            case "ConstructionCategoriesPage":
                HandleConstructionCategoriesPageResponse(responseID);
                break;
            case "BlueprintListPage":
                HandleBlueprintListPageResponse(responseID);
                break;
            case "BlueprintDetailsPage":
                HandleBlueprintDetailsPageResponse(responseID);
                break;

        }
    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Crafting Blueprints
                ChangePage("CraftCategoriesPage");
                break;
            case 2: // Construction Blueprints
                ChangePage("ConstructionCategoriesPage");
                break;
            case 3: // Back
                SwitchConversation("RestMenu");
                break;
        }
    }

    private void HandleCraftCategoriesPageResponse(int responseID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        CraftRepository craftRepo = new CraftRepository();
        ViewBlueprintsViewModel vm = (ViewBlueprintsViewModel) GetDialogCustomData();
        ClearPageResponses("BlueprintListPage");
        DialogResponse response = GetResponseByID("CraftCategoriesPage", responseID);
        int categoryID = (int)response.getCustomData();

        if(categoryID == -1) // Back
        {
            ChangePage("MainPage");
            return;
        }

        vm.setCraftBlueprints(craftRepo.GetPCBlueprintsByCategoryID(pcGO.getUUID(), categoryID));

        for(CraftBlueprintEntity bp: vm.getCraftBlueprints())
        {
            AddResponseToPage("BlueprintListPage", bp.getItemName(), true, bp.getCraftBlueprintID());
        }
        AddResponseToPage("BlueprintListPage", "Back", true, -1);

        vm.setMode(1);
        ChangePage("BlueprintListPage");
    }

    private void HandleConstructionCategoriesPageResponse(int responseID)
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        StructureRepository strucRepo = new StructureRepository();
        ViewBlueprintsViewModel vm = (ViewBlueprintsViewModel) GetDialogCustomData();
        ClearPageResponses("BlueprintListPage");
        DialogResponse response = GetResponseByID("ConstructionCategoriesPage", responseID);
        int categoryID = (int)response.getCustomData();

        if(categoryID == -1) // Back
        {
            ChangePage("MainPage");
            return;
        }

        vm.setStructureBlueprints(strucRepo.GetStructuresForPCByCategory(pcGO.getUUID(), categoryID));

        for(StructureBlueprintEntity bp: vm.getStructureBlueprints())
        {
            AddResponseToPage("BlueprintListPage", bp.getName(), true, bp.getStructureBlueprintID());
        }
        AddResponseToPage("BlueprintListPage", "Back", true, -1);

        vm.setMode(2);
        ChangePage("BlueprintListPage");
    }

    private void HandleBlueprintListPageResponse(int responseID)
    {
        ViewBlueprintsViewModel vm = (ViewBlueprintsViewModel)GetDialogCustomData();
        DialogResponse response = GetResponseByID("BlueprintListPage", responseID);
        int blueprintID = (int)response.getCustomData();

        if(blueprintID == -1)
        {
            if(vm.getMode() == 1)
                ChangePage("CraftCategoriesPage");
            else
                ChangePage("ConstructionCategoriesPage");
            return;
        }

        String header;
        if(vm.getMode() == 1)
        {
            header = CraftSystem.BuildBlueprintHeader(GetPC(), blueprintID);
        }
        else
        {
            header = StructureSystem.BuildMenuHeader(blueprintID);
        }

        SetPageHeader("BlueprintDetailsPage", header);
        ChangePage("BlueprintDetailsPage");
    }

    private void HandleBlueprintDetailsPageResponse(int responseID)
    {
        if(responseID == 1)
        {
            ChangePage("BlueprintListPage");
        }
    }

    @Override
    public void EndDialog() {

    }
}
