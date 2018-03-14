package Conversation;

import Dialog.*;
import Entities.*;
import Enumerations.SkillID;
import Enumerations.StructurePermission;
import GameObject.PlayerGO;
import GameSystems.SkillSystem;
import Helper.ColorToken;
import Conversation.ViewModels.ConstructionSiteMenuModel;
import Data.Repository.StructureRepository;
import GameSystems.PlayerAuthorizationSystem;
import GameSystems.StructureSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.List;

@SuppressWarnings("unused")
public class ConstructionSite extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage("<SET LATER>");
        DialogPage blueprintCategoryPage = new DialogPage(
                "Please select a blueprint category."
        );

        DialogPage blueprintDetailsPage = new DialogPage(
                "<SET LATER>",
                "Select Blueprint",
                "Preview",
                "Back"
        );

        DialogPage razePage = new DialogPage(
                ColorToken.Red() + "WARNING: " + ColorToken.End() + "Razing this construction site will destroy it permanently. Materials used will NOT be returned to you.\n\n" +
                        "Are you sure you want to raze this construction site?",
                ColorToken.Red() + "Confirm Raze" + ColorToken.End(),
                "Back"
        );

        DialogPage quickBuildPage = new DialogPage(
                "Quick building this structure will complete it instantly. Please use this sparingly.",
                "Confirm Quick Build",
                "Back"
        );

        DialogPage blueprintListPage = new DialogPage(
                "Please select a blueprint."
        );

        DialogPage rotatePage = new DialogPage(
                "Please select a rotation.",
                "Set Facing: East",
                "Set Facing: North",
                "Set Facing: South",
                "Set Facing: West",
                "Rotate 20\u00b0",
                "Rotate 30\u00b0",
                "Rotate 45\u00b0",
                "Rotate 60\u00b0",
                "Rotate 75\u00b0",
                "Rotate 90\u00b0",
                "Rotate 180\u00b0",
                "Back"
        );

        DialogPage recoverResourcesPage = new DialogPage(
                "This construction site is in an invalid state or location.\n\n" +
                "Possible reasons:\n" +
                "1.) The territory cannot manage any more structures.\n" +
                "2.) The area of influence of this construction site's territory flag blueprint overlaps with another territory's.\n\n" +
                "You may raze this construction site to recover all spent resources.",
                "Raze & Recover Resources"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("BlueprintCategoryPage", blueprintCategoryPage);
        dialog.addPage("BlueprintListPage", blueprintListPage);
        dialog.addPage("BlueprintDetailsPage", blueprintDetailsPage);
        dialog.addPage("QuickBuildPage", quickBuildPage);
        dialog.addPage("RotatePage", rotatePage);
        dialog.addPage("RazePage", razePage);
        dialog.addPage("RecoverResourcesPage", recoverResourcesPage);
        return dialog;
    }

    @Override
    public void Initialize() {

        InitializeConstructionSite();

        if(!StructureSystem.IsConstructionSiteValid(GetDialogTarget()))
        {
            ChangePage("RecoverResourcesPage");
        }
        else
        {
            BuildMainPage();
        }
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "BlueprintCategoryPage":
                HandleCategoryResponse(responseID);
                break;
            case "BlueprintListPage":
                HandleBlueprintListResponse(responseID);
                break;
            case "BlueprintDetailsPage":
                HandleBlueprintDetailsResponse(responseID);
                break;
            case "RazePage":
                HandleRazeResponse(responseID);
                break;
            case "QuickBuildPage":
                HandleQuickBuildResponse(responseID);
                break;
            case "RotatePage":
                HandleRotatePageResponse(responseID);
                break;
            case "RecoverResourcesPage":
                HandleRecoverResourcesPage(responseID);
                break;
        }
    }

    @Override
    public void EndDialog() {

    }

    private void InitializeConstructionSite()
    {
        NWObject site = GetDialogTarget();
        NWObject existingFlag = StructureSystem.GetTerritoryFlagOwnerOfLocation(NWScript.getLocation(GetDialogTarget()));
        StructureRepository repo = new StructureRepository();
        ConstructionSiteMenuModel model = new ConstructionSiteMenuModel();

        float distance = NWScript.getDistanceBetween(existingFlag, GetDialogTarget());
        if(existingFlag.equals(NWObject.INVALID))
        {
            model.setIsTerritoryFlag(true);
        }
        else
        {
            int flagID = StructureSystem.GetTerritoryFlagID(existingFlag);
            model.setFlagID(flagID);
            PCTerritoryFlagEntity entity = repo.GetPCTerritoryFlagByID(flagID);
            if(distance <= entity.getBlueprint().getMaxBuildDistance())
            {
                model.setIsTerritoryFlag(false);
            }
            else
            {
                model.setIsTerritoryFlag(true);
            }
        }

        model.setConstructionSiteID(StructureSystem.GetConstructionSiteID(site));
        SetDialogCustomData(model);
    }

    private ConstructionSiteMenuModel GetModel()
    {
        return (ConstructionSiteMenuModel)GetDialogCustomData();
    }

    private void BuildMainPage()
    {
        NWObject oPC = GetPC();
        DialogPage page = GetPageByName("MainPage");
        String header;
        ConstructionSiteMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();

        page.getResponses().clear();

        if(model.getConstructionSiteID() <= 0)
        {
            header = "Please select an option.";

            if(model.isTerritoryFlag())
            {
                page.addResponse("Select Blueprint", true);
                page.addResponse("Move", true);
                page.addResponse(ColorToken.Red() + "Raze" + ColorToken.End(), true);
            }
            else
            {
                page.addResponse("Select Blueprint", StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanBuildStructures, model.getFlagID()));
                page.addResponse("Move", StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanMoveStructures, model.getFlagID()));
                page.addResponse(ColorToken.Red() + "Raze" + ColorToken.End(), StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanRazeStructures, model.getFlagID()));
            }

        }
        else
        {
            ConstructionSiteEntity entity = repo.GetConstructionSiteByID(model.getConstructionSiteID());

            header = ColorToken.Green() + "Blueprint: " + ColorToken.End() + entity.getBlueprint().getName() + "\n";
            header += ColorToken.Green() + "Level: " + ColorToken.End() + entity.getBlueprint().getLevel() + "\n\n";

            if(entity.getBlueprint().getMaxBuildDistance() > 0.0f)
            {
                header += ColorToken.Green() + "Build Distance: " + ColorToken.End() + entity.getBlueprint().getMaxBuildDistance() + " meters" + "\n";
            }
            if(entity.getBlueprint().getMaxStructuresCount() > 0)
            {
                header += ColorToken.Green() + "Max # of Structures: " + ColorToken.End() + entity.getBlueprint().getMaxStructuresCount() + "\n";
            }
            if(entity.getBlueprint().getItemStorageCount() > 0)
            {
                header += ColorToken.Green() + "Item Storage: " + ColorToken.End() + entity.getBlueprint().getItemStorageCount() + " items" + "\n";
            }
            if(entity.getBlueprint().getResearchSlots() > 0)
            {
                header += ColorToken.Green() + "Research Slots: "  + ColorToken.End() + entity.getBlueprint().getResearchSlots() + " slots" + "\n";
            }
            if(entity.getBlueprint().getRpPerSecond() > 0)
            {
                header += ColorToken.Green() + "RP/Sec: " + ColorToken.End() + entity.getBlueprint().getRpPerSecond() + "\n";
            }

            header += ColorToken.Green() + "Resources Required: " + ColorToken.End() + "\n\n";

            for(ConstructionSiteComponentEntity comp: entity.getComponents())
            {
                //noinspection StringConcatenationInLoop
                header += comp.getQuantity() > 0 ? comp.getQuantity() + "x " + ItemHelper.GetNameByResref(comp.getStructureComponent().getResref()) + "\n" : "";
            }

            page.addResponse("Quick Build", PlayerAuthorizationSystem.IsPCRegisteredAsDM(GetPC()));
            page.addResponse("Preview", true);
            page.addResponse("Rotate", StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanRotateStructures, model.getFlagID()));
            page.addResponse("Move", StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanMoveStructures, model.getFlagID()));
            page.addResponse(ColorToken.Red() + "Raze" + ColorToken.End(), StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanRazeStructures, model.getFlagID()));
        }

        SetPageHeader("MainPage", header);
    }

    private void HandleMainPageResponse(int responseID)
    {
        ConstructionSiteMenuModel model = GetModel();

        if(model.getConstructionSiteID() <= 0)
        {
            switch (responseID)
            {
                case 1: // Select Blueprint

                    LoadCategoryPageResponses();
                    ChangePage("BlueprintCategoryPage");
                    break;
                case 2: // Move
                    StructureSystem.SetIsPCMovingStructure(GetPC(), GetDialogTarget(), true);
                    NWScript.floatingTextStringOnCreature("Please use your build tool to select a new location for this structure.", GetPC(), false);
                    EndConversation();
                    break;
                case 3: // Raze
                    ChangePage("RazePage");
                    break;
            }
        }
        else
        {
            switch (responseID)
            {
                case 1: // Quick Build
                    ChangePage("QuickBuildPage");
                    break;
                case 2: // Preview
                    DoConstructionSitePreview();
                    break;
                case 3: // Rotate
                    ChangePage("RotatePage");
                    break;
                case 4: // Move
                    StructureSystem.SetIsPCMovingStructure(GetPC(), GetDialogTarget(), true);
                    NWScript.floatingTextStringOnCreature("Please use your build tool to select a new location for this structure.", GetPC(), false);
                    EndConversation();
                    break;
                case 5: // Raze
                    ChangePage("RazePage");
                    break;
            }
        }

    }

    private void BuildBlueprintDetailsHeader()
    {
        ConstructionSiteMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();
        StructureBlueprintEntity entity = repo.GetStructureBlueprintByID(model.getBlueprintID());
        String header = ColorToken.Green() + "Blueprint Name: " + ColorToken.End() + entity.getName() + "\n";
        header += ColorToken.Green() + "Level: " + ColorToken.End() + entity.getLevel() + "\n\n";

        if(entity.getMaxBuildDistance() > 0.0f)
        {
            header += ColorToken.Green() + "Build Distance: " + ColorToken.End() + entity.getMaxBuildDistance() + " meters" + "\n";
        }
        if(entity.getMaxStructuresCount() > 0)
        {
            header += ColorToken.Green() + "Max # of Structures: " + ColorToken.End() + entity.getMaxStructuresCount() + "\n";
        }
        if(entity.getItemStorageCount() > 0)
        {
            header += ColorToken.Green() + "Item Storage: " + ColorToken.End() + entity.getItemStorageCount() + " items" + "\n";
        }

        if(!entity.getDescription().equals(""))
        {
            header += ColorToken.Green() + "Description: " + ColorToken.End() + entity.getDescription() + "\n\n";
        }
        header += (ColorToken.Green() + "Resources Required: " + ColorToken.End() + "\n") + "\n";


        for(StructureComponentEntity comp: entity.getComponents())
        {
            //noinspection StringConcatenationInLoop
            header += comp.getQuantity() > 0 ? comp.getQuantity() + "x " + ItemHelper.GetNameByResref(comp.getResref()) + "\n" : "";
        }


        SetPageHeader("BlueprintDetailsPage", header);

    }

    private void LoadCategoryPageResponses()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        ConstructionSiteMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();
        DialogPage page = GetPageByName("BlueprintCategoryPage");
        page.getResponses().clear();

        List<StructureCategoryEntity> categories = repo.GetStructureCategoriesByType(pcGO.getUUID(), model.isTerritoryFlag());

        for(StructureCategoryEntity category : categories)
        {
            page.addResponse(category.getName(), category.isActive(), category.getStructureCategoryID());
        }

        page.addResponse("Back", true);
    }

    private void LoadBlueprintListPageResponses()
    {
        PlayerGO pcGO = new PlayerGO(GetPC());
        ConstructionSiteMenuModel model = GetModel();
        StructureRepository repo = new StructureRepository();
        DialogPage page = GetPageByName("BlueprintListPage");
        page.getResponses().clear();
        NWLocation location = NWScript.getLocation(GetDialogTarget());
        int rank = SkillSystem.GetPCSkill(GetPC(), SkillID.Construction).getRank();

        List<StructureBlueprintEntity> entities = repo.GetStructuresByCategoryAndPlayerRank(pcGO.getUUID(), model.getCategoryID(), rank);
        for(StructureBlueprintEntity entity : entities)
        {
            String entityName = entity.getName() + " (Lvl. " + entity.getLevel() + ")";
            if(model.isTerritoryFlag())
            {
                if(StructureSystem.WillBlueprintOverlapWithExistingFlags(location, entity.getStructureBlueprintID()))
                {
                   entityName = ColorToken.Red() + entityName + " [OVERLAPS]" + ColorToken.End();
                }

            }
            page.addResponse(entityName, entity.isActive(), entity.getStructureBlueprintID());
        }

        page.addResponse("Back", true);
    }

    private void HandleCategoryResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintCategoryPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("MainPage");
            return;
        }

        ConstructionSiteMenuModel model = GetModel();
        model.setCategoryID((int) response.getCustomData());
        LoadBlueprintListPageResponses();
        ChangePage("BlueprintListPage");
    }

    private void HandleBlueprintListResponse(int responseID)
    {
        DialogResponse response = GetResponseByID("BlueprintListPage", responseID);
        if(response.getCustomData() == null)
        {
            ChangePage("BlueprintCategoryPage");
            return;
        }

        ConstructionSiteMenuModel model = GetModel();
        model.setBlueprintID((int) response.getCustomData());
        BuildBlueprintDetailsHeader();
        ChangePage("BlueprintDetailsPage");
    }

    private void HandleBlueprintDetailsResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Select Blueprint
                DoSelectBlueprint();
                break;
            case 2: // Preview
                DoBlueprintPreview(GetModel().getBlueprintID());
                break;
            case 3: // Back
                ChangePage("BlueprintListPage");
                break;
        }
    }

    private void HandleRotatePageResponse(int responseID)
    {
        switch(responseID)
        {
            case 1: // East
                DoRotateConstructionSite(0.0f, true);
                break;
            case 2: // North
                DoRotateConstructionSite(90.0f, true);
                break;
            case 3: // South
                DoRotateConstructionSite(180.0f, true);
                break;
            case 4: // West
                DoRotateConstructionSite(270.0f, true);
                break;
            case 5: // Rotate 20
                DoRotateConstructionSite(20.0f, false);
                break;
            case 6: // Rotate 30
                DoRotateConstructionSite(30.0f, false);
                break;
            case 7: // Rotate 45
                DoRotateConstructionSite(45.0f, false);
                break;
            case 8: // Rotate 60
                DoRotateConstructionSite(60.0f, false);
                break;
            case 9: // Rotate 75
                DoRotateConstructionSite(75.0f, false);
                break;
            case 10: // Rotate 90
                DoRotateConstructionSite(90.0f, false);
                break;
            case 11: // Rotate 180
                DoRotateConstructionSite(180.0f, false);
                break;
            case 12: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleRazeResponse(int responseID)
    {
        switch(responseID)
        {
            case 1: // Confirm Raze
                DoRaze();
                break;
            case 2: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleQuickBuildResponse(int responseID)
    {
        switch(responseID)
        {
            case 1:
                DoQuickBuild();
                break;
            case 2: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void DoBlueprintPreview(int blueprintID)
    {
        final ConstructionSiteMenuModel model = GetModel();
        if(model.isPreviewing()) return;
        model.setIsPreviewing(true);
        StructureRepository repo = new StructureRepository();
        StructureBlueprintEntity entity = repo.GetStructureBlueprintByID(blueprintID);
        final NWObject preview = NWScript.createObject(ObjectType.PLACEABLE, entity.getResref(), NWScript.getLocation(GetDialogTarget()), false, "");
        NWScript.setUseableFlag(preview, false);
        NWScript.setPlotFlag(preview, true);
        NWScript.destroyObject(preview, 6.0f);

        Scheduler.delay(preview, 6000, () -> model.setIsPreviewing(false));

    }

    private void DoConstructionSitePreview()
    {
        final ConstructionSiteMenuModel model = GetModel();
        if(model.isPreviewing()) return;
        StructureRepository repo = new StructureRepository();
        ConstructionSiteEntity entity = repo.GetConstructionSiteByID(model.getConstructionSiteID());
        StructureBlueprintEntity blueprint = entity.getBlueprint();
        final NWObject preview = NWScript.createObject(ObjectType.PLACEABLE, blueprint.getResref(), NWScript.getLocation(GetDialogTarget()), false, "");
        NWScript.setUseableFlag(preview, false);
        NWScript.setPlotFlag(preview, true);
        NWScript.destroyObject(preview, 6.0f);

        Scheduler.delay(preview, 6000, () -> model.setIsPreviewing(false));
    }

    private void DoRaze()
    {
        ConstructionSiteMenuModel model = GetModel();
        NWObject oPC = GetPC();

        if(!StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanRazeStructures, model.getFlagID()))
        {
            NWScript.floatingTextStringOnCreature("You do not have permission to raze structures.", oPC, false);
            BuildMainPage();
            ChangePage("MainPage");
            return;
        }

        StructureSystem.RazeConstructionSite(GetPC(), GetDialogTarget(), false);
        EndConversation();
    }

    private void DoQuickBuild()
    {
        NWObject completedStructure = StructureSystem.CompleteStructure(GetDialogTarget());
        StructureSystem.LogQuickBuildAction(GetPC(), completedStructure);
        EndConversation();
    }

    private void DoSelectBlueprint()
    {
        NWObject oPC = GetPC();
        ConstructionSiteMenuModel model = GetModel();

        if(!StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanBuildStructures, model.getFlagID()))
        {
            NWScript.floatingTextStringOnCreature("You do not have permission to build structures.", oPC, false);
            BuildMainPage();
            ChangePage("MainPage");
            return;
        }

        if(model.isTerritoryFlag() &&
                StructureSystem.WillBlueprintOverlapWithExistingFlags(NWScript.getLocation(GetDialogTarget()), model.getBlueprintID()))
        {
            NWScript.floatingTextStringOnCreature("Unable to select blueprint. Area of influence would overlap with another territory.", GetPC(), false);
        }
        else
        {
            StructureSystem.SelectBlueprint(GetPC(), GetDialogTarget(), model.getBlueprintID());
            NWScript.floatingTextStringOnCreature("Blueprint set. Equip a hammer and 'bash' the construction site to build.", GetPC(), false);
            EndConversation();
        }
    }


    private void DoRotateConstructionSite(float rotation, boolean isSet)
    {
        NWObject oPC = GetPC();
        ConstructionSiteMenuModel model = GetModel();

        if(!StructureSystem.PlayerHasPermission(oPC, StructurePermission.CanRotateStructures, model.getFlagID()))
        {
            NWScript.floatingTextStringOnCreature("You do not have permission to rotate structures.", oPC, false);
            BuildMainPage();
            ChangePage("MainPage");
            return;
        }

        StructureRepository repo = new StructureRepository();
        int constructionSiteID = StructureSystem.GetConstructionSiteID(GetDialogTarget());
        final ConstructionSiteEntity entity = repo.GetConstructionSiteByID(constructionSiteID);

        if(isSet)
        {
            entity.setLocationOrientation(rotation);
        }
        else
        {
            entity.setLocationOrientation(entity.getLocationOrientation() + rotation);
        }

        repo.Save(entity);

        Scheduler.assign(GetDialogTarget(), () -> NWScript.setFacing((float) entity.getLocationOrientation()));

    }

    private void HandleRecoverResourcesPage(int responseID)
    {
        switch(responseID)
        {
            case 1: // Raze & Recover Resources
                StructureSystem.RazeConstructionSite(GetPC(), GetDialogTarget(), true);
                EndConversation();
                break;
        }
    }
}
