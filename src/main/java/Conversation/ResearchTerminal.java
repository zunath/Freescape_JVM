package Conversation;

import Conversation.ResponseModels.ResearchTerminalResponse;
import Conversation.ViewModels.ResearchTerminalViewModel;
import Data.Repository.CraftRepository;
import Data.Repository.ResearchRepository;
import Data.Repository.StructureRepository;
import Dialog.*;
import Entities.*;
import GameSystems.MagicSystem;
import GameSystems.StructureSystem;
import Helper.ColorToken;
import Helper.TimeHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

import java.util.List;

@SuppressWarnings("unused")
public class ResearchTerminal extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                ColorToken.Green() + "Research Terminal Menu" + ColorToken.End());

        DialogPage instructionsPage = new DialogPage(
                "Please select a topic.",
                "How does researching work?",
                "How long do blueprints take to research?",
                "What are 'Research Slots'?",
                "What is RP/Sec?",
                "What happens when a research job is canceled?",
                "Back"
        );

        DialogPage selectCraftPage = new DialogPage(
                "Please select a craft."
        );

        DialogPage selectCategoryPage = new DialogPage(
                "Please select a category. You can learn more blueprints by upgrading your Researching skill."
        );

        DialogPage selectBlueprintPage = new DialogPage(
                "Please select a blueprint. You can learn more blueprints by upgrading your Researching skill."
        );

        DialogPage blueprintDetailsPage = new DialogPage(
                "[SET LATER]",
                "Select Blueprint",
                "Back"
        );

        DialogPage deliverBlueprintPage = new DialogPage(
                "This blueprint is finished researching. To retrieve the finished product select 'Deliver Blueprint' below.",
                "Deliver Blueprint",
                "Back"
        );

        DialogPage researchInProgressPage = new DialogPage(
                "[SET LATER]",
                "Cancel Research",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("InstructionsPage", instructionsPage);
        dialog.addPage("SelectCraftPage", selectCraftPage);
        dialog.addPage("SelectCategoryPage", selectCategoryPage);
        dialog.addPage("SelectBlueprintPage", selectBlueprintPage);
        dialog.addPage("BlueprintDetailsPage", blueprintDetailsPage);
        dialog.addPage("DeliverBlueprintPage", deliverBlueprintPage);
        dialog.addPage("ResearchInProgressPage", researchInProgressPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        NWObject oPC = GetPC();

        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC) || NWScript.getIsDMPossessed(oPC))
        {
            NWScript.sendMessageToPC(oPC, "Only player characters may use this terminal.");
            EndConversation();
            return;
        }

        ResearchTerminalViewModel model = new ResearchTerminalViewModel();
        int researchLevel = 0; // TODO: Skill system check
        model.setPlayerResearchLevel(researchLevel);
        SetDialogCustomData(model);

        LoadSlotResponses();
        LoadSelectCraftPageResponses();
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageResponses(responseID);
                break;
            case "InstructionsPage":
                HandleInstructionPageResponses(responseID);
                break;
            case "SelectCraftPage":
                HandleSelectCraftPageResponses(responseID);
                break;
            case "SelectCategoryPage":
                HandleSelectCategoryPageResponses(responseID);
                break;
            case "SelectBlueprintPage":
                HandleSelectBlueprintPageResponses(responseID);
                break;
            case "BlueprintDetailsPage":
                HandleBlueprintDetailsPageResponses(responseID);
                break;
            case "DeliverBlueprintPage":
                HandleDeliverBlueprintPageResponses(responseID);
                break;
            case "ResearchInProgressPage":
                HandleResearchInProgressPageResponses(responseID);
                break;
        }
    }

    @Override
    public void EndDialog() {

    }

    private ResearchTerminalViewModel GetModel()
    {
        return (ResearchTerminalViewModel) GetDialogCustomData();
    }

    private void LoadSlotResponses()
    {
        ResearchRepository researchRepo = new ResearchRepository();
        StructureRepository structureRepo = new StructureRepository();
        NWObject terminal = GetDialogTarget();
        int structureID = StructureSystem.GetPlaceableStructureID(terminal);
        PCTerritoryFlagStructureEntity entity = structureRepo.GetPCStructureByID(structureID);
        int numberOfSlots = entity.getBlueprint().getResearchSlots();
        DialogPage page = GetPageByName("MainPage");
        List<PCTerritoryFlagsStructuresResearchQueueEntity> queue = researchRepo.GetResearchJobsInQueue(structureID);
        DateTime now = DateTime.now(DateTimeZone.UTC);

        page.getResponses().clear();
        page.addResponse(ColorToken.Green() + "Refresh" + ColorToken.End(), true);
        page.addResponse("Read Instructions", true);

        for(int index = 1; index <= numberOfSlots; index++)
        {
            PCTerritoryFlagsStructuresResearchQueueEntity validQueue = null;
            for(PCTerritoryFlagsStructuresResearchQueueEntity currentQueue : queue)
            {
                if(currentQueue.getResearchSlot() == index)
                {
                    validQueue = currentQueue;
                    break;
                }
            }

            ResearchTerminalResponse responseModel = new ResearchTerminalResponse();
            responseModel.setSlotNumber(index);

            if(validQueue == null)
            {
                responseModel.setReadyToDeliver(false);
                page.addResponse("Slot #" + index + ": Available", true, responseModel);
            }
            else
            {
                DateTime completionDateTime = new DateTime(validQueue.getCompletedDateTime());
                String completionDelay = BuildCompletionDelayString(completionDateTime);
                responseModel.setResearching(true);

                // Still waiting for completion.
                if(completionDateTime.isAfter(now))
                {
                    responseModel.setReadyToDeliver(false);
                    page.addResponse("Slot #" + index + ": " + validQueue.getBlueprint().getCraftBlueprint().getItemName() + " [" + completionDelay + "]", true, responseModel);
                }
                // Ready to deliver item.
                else
                {
                    responseModel.setReadyToDeliver(true);
                    page.addResponse("Slot #" + index + ": " + validQueue.getBlueprint().getCraftBlueprint().getItemName() + " [Ready to Deliver]", true, responseModel);
                }
            }


        }
    }

    private String BuildCompletionDelayString(DateTime completionDateTime)
    {
        DateTime now = DateTime.now(DateTimeZone.UTC);
        return TimeHelper.GetTimeToWaitShortIntervals(now, completionDateTime, true);
    }

    private void LoadSelectCraftPageResponses()
    {
        CraftRepository repo = new CraftRepository();
        List<CraftEntity> crafts = repo.GetAllCrafts();
        DialogPage page = GetPageByName("SelectCraftPage");
        page.getResponses().clear();

        for(CraftEntity craft : crafts)
        {
            page.addResponse(craft.getName(), true, craft.getCraftID());
        }
        page.addResponse("Back", true, -1);
    }

    private void HandleMainPageResponses(int responseID)
    {
        if(responseID == 1) // Refresh
        {
            LoadSlotResponses();
        }
        else if(responseID == 2) // Read Instructions
        {
            ChangePage("InstructionsPage");
        }
        else
        {
            ResearchRepository repo = new ResearchRepository();
            DialogResponse response = GetResponseByID("MainPage", responseID);
            ResearchTerminalResponse responseModel = (ResearchTerminalResponse) response.getCustomData();
            ResearchTerminalViewModel model = GetModel();
            model.setSelectedSlot(responseModel.getSlotNumber());

            if(responseModel.isResearching())
            {
                if(responseModel.isReadyToDeliver())
                {
                    ChangePage("DeliverBlueprintPage");
                }
                else{
                    int structureID = StructureSystem.GetPlaceableStructureID(GetDialogTarget());
                    PCTerritoryFlagsStructuresResearchQueueEntity queue = repo.GetResearchJobInQueueForSlot(structureID, responseModel.getSlotNumber());
                    SetPageHeader("ResearchInProgressPage", BuildBlueprintDetailsHeaderString(queue.getBlueprint().getResearchBlueprintID()));
                    ChangePage("ResearchInProgressPage");
                }
            }

            else {

                ChangePage("SelectCraftPage");
            }
        }
    }

    private void HandleInstructionPageResponses(int responseID)
    {
        String header = "";
        switch(responseID)
        {
            case 1: // How does researching work?
                header += "Researching involves creating new crafting blueprints using raw materials.\n\n";
                header += "To start researching you can follow the steps below:\n";
                header += "1.) Purchase upgrades in the Researching skill via the Rest Menu (press 'R' or use your omni-tool). This will unlock additional blueprints to create.\n";
                header += "2.) Locate a research terminal with an open 'Research Slot'.\n";
                header += "3.) Select the open Research Slot and pick a blueprint to create.\n";
                header += "4.) Research happens in real-time and does not require any further interaction.\n";
                header += "5.) Once research has completed you can retrieve the blueprint by selecting the slot and choosing 'Deliver Goods'.\n\n";
                header += "Please note that research jobs can be canceled at any time but the raw materials required will not be returned to you.";
                SetPageHeader("InstructionsPage", header);
                break;
            case 2: // How long do blueprints take to research?
                header += "The length of time a blueprint takes to research is largely determined by two variables:\n\n";
                header += "1.) The difficulty of the blueprint. Higher level blueprints take longer to research.\n";
                header += "2.) The RP/Sec rating of the Research Terminal being used.\n\n";
                header += "Additionally, other factors may enhance your ability to research. These include 'Booster' structures and upgrades available to your character.";
                SetPageHeader("InstructionsPage", header);
                break;
            case 3: // What are 'Research Slots'?
                header += "Research Slots are limited resources which enable you to research new blueprints.\n\n";
                header += "All research terminals have at least one slot and research as fast as the overall RP/Sec rating for the terminal.";
                SetPageHeader("InstructionsPage", header);
                break;
            case 4: // What is 'RP/Sec'?
                header += "RP/Sec stands for 'Research Points per second'. This represents how fast blueprints will research on the terminal.\n\n";
                header += "In short, the higher the RP/Sec, the faster the research. All terminals have at least 1 RP/Sec but some have higher rates.\n\n";
                header += "Additionally, the RP/Sec may be increased via 'Booster' structures as well as upgrades available to your character.";
                SetPageHeader("InstructionsPage", header);
                break;
            case 5: // What happens when a research job is canceled?
                header += "If you choose to cancel a research job, all progress on that blueprint will immediately stop.\n\n";
                header += "The slot which was in use will now be free and another job can take its place.\n\n";
                header += "No refunds will be given if a job is canceled so be absolutely positive before taking that action.";
                SetPageHeader("InstructionsPage", header);
                break;
            case 6:
                SetPageHeader("InstructionsPage", "Please select a topic.");
                ChangePage("MainPage");
                break;
        }


    }

    private void HandleSelectCraftPageResponses(int responseID)
    {
        DialogResponse response = GetResponseByID("SelectCraftPage", responseID);

        if((Integer)response.getCustomData() == -1) // Back
        {
            ChangePage("MainPage");
            return;
        }

        ResearchTerminalViewModel model = GetModel();
        model.setSelectedCraftID((Integer)response.getCustomData());
        LoadSelectCategoryResponses();
        ChangePage("SelectCategoryPage");
    }

    private void LoadSelectCategoryResponses()
    {
        ResearchTerminalViewModel model = GetModel();

        ResearchRepository researchRepository = new ResearchRepository();
        List<CraftBlueprintCategoryEntity> categories = researchRepository.GetResearchCategoriesAvailableForCraftAndLevel(model.getSelectedCraftID(), model.getPlayerResearchLevel());

        DialogPage page = GetPageByName("SelectCategoryPage");
        page.getResponses().clear();

        for(CraftBlueprintCategoryEntity category: categories)
        {
            page.addResponse(category.getName(), true, category.getCraftBlueprintCategoryID());
        }

        page.addResponse("Back", true, -1);

    }

    private void HandleSelectCategoryPageResponses(int responseID)
    {
        DialogResponse response = GetResponseByID("SelectCategoryPage", responseID);

        if((Integer)response.getCustomData() == -1)
        {
            ChangePage("SelectCraftPage");
            return;
        }

        ResearchTerminalViewModel model = GetModel();
        model.setSelectedCraftCategoryID((Integer)response.getCustomData());
        LoadSelectBlueprintResponses();
        ChangePage("SelectBlueprintPage");
    }

    private void LoadSelectBlueprintResponses()
    {
        ResearchTerminalViewModel model = GetModel();
        ResearchRepository researchRepository = new ResearchRepository();
        DialogPage page = GetPageByName("SelectBlueprintPage");
        page.getResponses().clear();
        List<ResearchBlueprintEntity> blueprints = researchRepository.GetResearchBlueprintsForCategory(model.getSelectedCraftID(), model.getSelectedCraftCategoryID(), model.getPlayerResearchLevel());

        for(ResearchBlueprintEntity bp : blueprints)
        {
            page.addResponse(bp.getCraftBlueprint().getItemName(), true, bp.getResearchBlueprintID());
        }

        page.addResponse("Back", true, -1);

    }

    private void HandleSelectBlueprintPageResponses(int responseID)
    {
        DialogResponse response = GetResponseByID("SelectBlueprintPage", responseID);

        if((Integer)response.getCustomData() == -1)
        {
            ChangePage("SelectCategoryPage");
            return;
        }

        ResearchTerminalViewModel model = GetModel();
        model.setSelectedBlueprintID((Integer)response.getCustomData());
        SetBlueprintDetailsHeader();
        ChangePage("BlueprintDetailsPage");
    }

    private String BuildBlueprintDetailsHeaderString(int blueprintID)
    {
        ResearchRepository researchRepo = new ResearchRepository();
        ResearchBlueprintEntity bp = researchRepo.GetResearchBlueprintByID(blueprintID);
        long secondsToResearch = CalculateNumberOfSecondsToResearch(bp);
        DateTime completionDate = DateTime.now(DateTimeZone.UTC).plusSeconds((int)secondsToResearch);
        int defaultPrice = bp.getPrice();
        int adjustedPrice = bp.getPrice();

        String header = ColorToken.Green() + "Blueprint: " + ColorToken.End() + bp.getCraftBlueprint().getItemName() + "\n";
        header += ColorToken.Green() + "Price: " + ColorToken.End() +  defaultPrice  + "\n";
        if(defaultPrice != adjustedPrice)
        {
            header += ColorToken.Green() + "Your Price: " + ColorToken.End() + adjustedPrice + "\n";
        }

        header += ColorToken.Green() + "Skill Required: " + ColorToken.End() + bp.getSkillRequired() + "\n";
        header += ColorToken.Green() + "Research Time: " + ColorToken.End() + BuildCompletionDelayString(completionDate) + "\n";


        return header;
    }

    private void SetBlueprintDetailsHeader()
    {
        ResearchTerminalViewModel model = GetModel();
        SetPageHeader("BlueprintDetailsPage", BuildBlueprintDetailsHeaderString(model.getSelectedBlueprintID()));
    }

    private long CalculateNumberOfSecondsToResearch(ResearchBlueprintEntity blueprint)
    {
        StructureRepository structureRepo = new StructureRepository();
        NWObject terminal = GetDialogTarget();
        int structureID = StructureSystem.GetPlaceableStructureID(terminal);
        StructureBlueprintEntity structureBP = structureRepo.GetPCStructureByID(structureID).getBlueprint();
        int rpPerSec = structureBP.getRpPerSecond();
        long rpRequired = blueprint.getResearchPoints();

        return rpRequired / rpPerSec;
    }

    private void HandleBlueprintDetailsPageResponses(int responseID)
    {
        ResearchTerminalViewModel model = GetModel();

        switch(responseID)
        {
            case 1:
                if(model.isConfirmingBlueprintSelection())
                {
                    model.setConfirmingBlueprintSelection(false);
                    SetResponseText("BlueprintDetailsPage", 1, "Select Blueprint");
                    DoSelectBlueprint();
                }
                else {
                    model.setConfirmingBlueprintSelection(true);
                    SetResponseText("BlueprintDetailsPage", 1, "CONFIRM SELECT BLUEPRINT");
                }
                break;
            case 2: // Back
                model.setConfirmingBlueprintSelection(false);
                SetResponseText("BlueprintDetailsPage", 1, "Select Blueprint");
                ChangePage("SelectBlueprintPage");
                break;
        }
    }

    private void DoSelectBlueprint()
    {
        NWObject oPC = GetPC();
        ResearchTerminalViewModel model = GetModel();
        int gold = NWScript.getGold(oPC);
        ResearchRepository repo = new ResearchRepository();
        ResearchBlueprintEntity bp = repo.GetResearchBlueprintByID(model.getSelectedBlueprintID());
        int finalPrice = bp.getPrice();

        if(gold < finalPrice)
        {
            NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You do not have enough money to research that blueprint." + ColorToken.End(), oPC, false);
            model.setConfirmingBlueprintSelection(false);
            SetResponseText("BlueprintDetailsPage", 1, "Select Blueprint");
            return;
        }

        NWScript.takeGoldFromCreature(finalPrice, oPC, true);
        int structureID = StructureSystem.GetPlaceableStructureID(GetDialogTarget());
        long secondsToResearch = CalculateNumberOfSecondsToResearch(bp);
        DateTime completionDate = DateTime.now(DateTimeZone.UTC).plusSeconds((int)secondsToResearch);

        PCTerritoryFlagsStructuresResearchQueueEntity entity = new PCTerritoryFlagsStructuresResearchQueueEntity();
        entity.setBlueprint(bp);
        entity.setCanceled(false);
        entity.setDeliverDateTime(null);
        entity.setCompletedDateTime(completionDate.toDate());
        entity.setResearchSlot(model.getSelectedSlot());
        entity.setPcStructureID(structureID);
        entity.setStartDate(DateTime.now(DateTimeZone.UTC).toDate());
        repo.Save(entity);

        LoadSlotResponses();
        ChangePage("MainPage");

    }

    private void HandleDeliverBlueprintPageResponses(int responseID)
    {
        switch (responseID)
        {
            case 1: // Deliver Blueprint
                DoDeliverBlueprint();
                break;
            case 2: // Back
                LoadSlotResponses();
                ChangePage("MainPage");
                break;
        }
    }

    private void DoDeliverBlueprint()
    {
        ResearchTerminalViewModel model = GetModel();
        int structureID = StructureSystem.GetPlaceableStructureID(GetDialogTarget());
        ResearchRepository repo = new ResearchRepository();
        PCTerritoryFlagsStructuresResearchQueueEntity queue = repo.GetResearchJobInQueueForSlot(structureID, model.getSelectedSlot());

        String resref = "blueprint_" + queue.getBlueprint().getCraftBlueprint().getCraftBlueprintID();
        NWScript.createItemOnObject(resref, GetPC(), 1, "");

        queue.setDeliverDateTime(DateTime.now(DateTimeZone.UTC).toDate());
        repo.Save(queue);

        LoadSlotResponses();
        ChangePage("MainPage");
    }

    private void HandleResearchInProgressPageResponses(int responseID)
    {
        ResearchTerminalViewModel model = GetModel();

        switch(responseID)
        {
            case 1: // Cancel Blueprint
                if(model.isConfirmingCancelResearch())
                {
                    model.setConfirmingCancelResearch(false);
                    SetResponseText("ResearchInProgressPage", 1, "Cancel Research");
                    DoCancelResearch();
                    LoadSlotResponses();
                    ChangePage("MainPage");
                }
                else {
                    model.setConfirmingCancelResearch(true);
                    SetResponseText("ResearchInProgressPage", 1, "CONFIRM CANCEL RESEARCH");
                }
                break;
            case 2: // Back
                model.setConfirmingCancelResearch(false);
                SetResponseText("ResearchInProgressPage", 1, "Cancel Research");
                LoadSlotResponses();
                ChangePage("MainPage");
                break;
        }
    }

    private void DoCancelResearch()
    {
        ResearchTerminalViewModel model = GetModel();
        int structureID = StructureSystem.GetPlaceableStructureID(GetDialogTarget());
        ResearchRepository repo = new ResearchRepository();
        PCTerritoryFlagsStructuresResearchQueueEntity queue = repo.GetResearchJobInQueueForSlot(structureID, model.getSelectedSlot());
        queue.setCanceled(true);
        repo.Save(queue);

        LoadSlotResponses();
        ChangePage("MainPage");
    }
}
