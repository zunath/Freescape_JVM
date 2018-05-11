package Conversation;

import Data.Repository.StructureRepository;
import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Entities.BuildingOwnerEntity;
import GameObject.PlayerGO;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class BuildingExit extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "Please select an option.",
                "Exit the building",
                "Peek outside",
                "Adjust Building Permissions"
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        NWObject oPC = GetPC();
        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject door = GetDialogTarget();
        NWObject area = getArea(door);
        NWLocation location = getLocation(door);
        StructureRepository repo = new StructureRepository();
        NWObject flag = StructureSystem.GetTerritoryFlagOwnerOfLocation(location);
        int territoryFlagID = StructureSystem.GetTerritoryFlagID(flag);
        int structureID = StructureSystem.GetPlaceableStructureID(door);

        // Only territory owner or building owner may adjust permissions.
        BuildingOwnerEntity owners = repo.GetBuildingOwners(territoryFlagID, structureID);
        if(!pcGO.getUUID().equals(owners.getTerritoryOwner()) &&
                pcGO.getUUID().equals(owners.getBuildingOwner()))
        {
            SetResponseVisible("MainPage", 3, false);
        }

        if(getLocalInt(area, "IS_BUILDING_PREVIEW") == 1)
        {
            SetResponseVisible("MainPage", 2, false);
            SetResponseVisible("MainPage", 3, false);
        }

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
            case 1: // Exit the building
                DoExitBuilding();
                break;
            case 2: // Peek outside
                DoPeekOutside();
                break;
            case 3: // Adjust building permissions
                SwitchConversation("TerritoryFlag");
                break;
        }
    }

    private void DoExitBuilding()
    {
        NWObject door = GetDialogTarget();
        NWObject oArea = getArea(door);
        NWLocation location = getLocalLocation(door, "PLAYER_HOME_EXIT_LOCATION");

        Scheduler.assignNow(GetPC(), () -> actionJumpToLocation(location));

        Scheduler.delay(door, 1000, () -> {
            // If a player is still inside the instance, don't destroy it.
            for(NWObject player: getPCs())
            {
                if(getArea(player).equals(oArea))
                {
                    return;
                }
            }
            destroyArea(oArea);
        });

    }

    private void DoPeekOutside()
    {
        final float MaxDistance = 2.5f;
        NWObject door = GetDialogTarget();
        NWLocation location = getLocalLocation(door, "PLAYER_HOME_EXIT_LOCATION");

        int numberFound = 0;
        int nth = 1;
        NWObject nearest = getNearestObjectToLocation(ObjectType.CREATURE, location, nth);
        while(getIsObjectValid(nearest))
        {
            if(getDistanceBetweenLocations(location, getLocation(nearest)) > MaxDistance) break;

            if(getIsPC(nearest) && !getIsDM(nearest))
            {
                numberFound++;
            }

            nth++;
            nearest = getNearestObjectToLocation(ObjectType.CREATURE, location, nth);
        }

        if(numberFound <= 0)
        {
            floatingTextStringOnCreature("You don't see anyone outside.", GetPC(), false);
        }
        else if(numberFound == 1)
        {
            floatingTextStringOnCreature("You see one person outside.", GetPC(), false);
        }
        else
        {
            floatingTextStringOnCreature("You see " + numberFound + " people outside.", GetPC(), false);
        }

    }

    @Override
    public void EndDialog() {

    }
}
