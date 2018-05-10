package Conversation;

import Data.Repository.PlayerRepository;
import Data.Repository.StructureRepository;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.*;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;
import static org.nwnx.nwnx2.jvm.NWScript.setLocalInt;

public class BuildingEntrance extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select an option.",
                "Enter the building",
                "Knock on the door");

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {

    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch (pageName)
        {
            case "MainPage":
                HandleMainPageResponses(responseID);
                break;
        }
    }

    private void HandleMainPageResponses(int responseID)
    {
        switch (responseID)
        {
            case 1: // Enter the building
                DoEnterBuilding();
                break;
            case 2: // Knock on the door
                DoKnockOnDoor();
                break;
        }
    }

    private void DoEnterBuilding()
    {
        NWObject oPC = GetPC();
        NWObject door = GetDialogTarget();

        int structureID = StructureSystem.GetPlaceableStructureID(door);

        if(structureID <= 0)
        {
            floatingTextStringOnCreature("ERROR: Door doesn't have a structure ID assigned. Notify an admin about this issue.", oPC, false);
            return;
        }

        StructureRepository structureRepo = new StructureRepository();
        PCTerritoryFlagStructureEntity structure = structureRepo.GetPCStructureByID(structureID);
        BuildingInteriorEntity interior = structure.getBuildingInterior();

        if(interior == null)
        {
            floatingTextStringOnCreature("ERROR: Could not locate interior object for structure. Notify an admin about this issue.", oPC, false);
            return;
        }

        NWObject instance = GetAreaInstance(structureID);

        if(instance == null)
        {
            PCTerritoryFlagEntity flag = structureRepo.GetPCTerritoryFlagByBuildingStructureID(structureID);

            String name = structure.getCustomName();
            if(name.equals(""))
            {
                PlayerRepository playerRepo = new PlayerRepository();
                PlayerEntity owner = playerRepo.GetByPlayerID(structure.getPcTerritoryFlag().getPlayerID());
                name = owner.getCharacterName() + "'s Building";
            }

            instance = createArea(interior.getAreaResref(), "", name);
            setLocalInt(instance, "BUILDING_STRUCTURE_ID", structureID);
            setLocalInt(instance, "TERRITORY_FLAG_ID", flag.getPcTerritoryFlagID());

            // Load structures & construction sites
            for(ConstructionSiteEntity entity: flag.getConstructionSites())
            {
                StructureSystem.CreateConstructionSiteFromEntity(entity);
            }
            for(PCTerritoryFlagStructureEntity entity: flag.getStructures())
            {
                StructureSystem.CreateStructureFromEntity(entity);
            }

        }

        setLocalInt(instance, "BUILDING_DISABLED", 0);
        StructureSystem.JumpPCToBuildingInterior(oPC, instance);
    }


    private NWObject GetAreaInstance(int buildingStructureID)
    {
        NWObject instance = null;
        NWObject area = getFirstArea();
        while(getIsObjectValid(area))
        {
            if(getLocalInt(area, "BUILDING_STRUCTURE_ID") == buildingStructureID)
            {
                instance = area;
                break;
            }

            area = getNextArea();
        }

        return instance;
    }



    private void DoKnockOnDoor()
    {
        NWObject door = GetDialogTarget();
        int structureID = StructureSystem.GetPlaceableStructureID(door);
        NWObject instance = GetAreaInstance(structureID);

        floatingTextStringOnCreature("You knock on the door.", GetPC(), false);

        if(instance != null)
        {
            NWObject[] players = getPCs();

            for(NWObject player: players)
            {
                if(getArea(player).equals(instance))
                {
                    floatingTextStringOnCreature("Someone is knocking on the front door.", player, false);
                }
            }
        }

    }


    @Override
    public void EndDialog() {

    }
}
