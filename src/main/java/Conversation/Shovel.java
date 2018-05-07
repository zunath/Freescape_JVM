package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Enumerations.SkillID;
import GameSystems.FarmingSystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Shovel extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                "<SET LATER>",
                "Dig a hole",
                "Retrieve Seed"
        );

        DialogPage harvestPage = new DialogPage(
                "Are you sure you want to harvest this plant? Harvesting will destroy the plant and recover a seed.",
                "Yes, harvest it.",
                "Back");

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("HarvestPage", harvestPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        NWObject shovel = getLocalObject(GetPC(), "SHOVEL_ITEM");
        int charges = getItemCharges(shovel);

        String header = "This shovel has " + charges + " uses remaining. What would you like to do?";
        SetPageHeader("MainPage", header);

        if(!CanHarvest())
        {
            SetResponseVisible("MainPage", 2, false);
        }
    }

    private boolean CanHarvest()
    {
        NWObject target = getLocalObject(GetPC(), "SHOVEL_TARGET_OBJECT");

        return getIsObjectValid(target) &&
                getLocalInt(target, "GROWING_PLANT_ID") > 0 &&
                getDistanceBetween(GetPC(), target) <= 2.0f;
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleMainPageResponse(responseID);
                break;
            case "HarvestPage":
                HandleHarvestPageResponse(responseID);
                break;
        }
    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Dig a Hole
                DigAHole();
                break;
            case 2: // Harvest Plant
                ChangePage("HarvestPage");
                break;
        }
    }

    private void DigAHole()
    {
        NWObject shovel = getLocalObject(GetPC(), "SHOVEL_ITEM");
        NWLocation targetLocation = getLocalLocation(GetPC(), "SHOVEL_TARGET_LOCATION");
        int charges = getItemCharges(shovel) - 1;

        if(charges <= 0)
        {
            destroyObject(shovel, 0.0f);
        }
        else
        {
            setItemCharges(shovel, charges);
        }

        createObject(ObjectType.PLACEABLE, "farm_small_hole", targetLocation, false, "");
        floatingTextStringOnCreature("You dig a hole.", GetPC(), false);
        SkillSystem.GiveSkillXP(GetPC(), SkillID.Farming, 50);
        EndConversation();
    }

    private void HandleHarvestPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Harvest Seed
                HarvestSeed();
                break;
            case 2: // Back
                ChangePage("MainPage");
                break;
        }
    }

    private void HarvestSeed()
    {
        if(!CanHarvest())
        {
            sendMessageToPC(GetPC(), "You cannot harvest that plant from here. Move closer and try again.");
            return;
        }

        NWObject shovel = getLocalObject(GetPC(), "SHOVEL_ITEM");
        NWObject plant = getLocalObject(GetPC(), "SHOVEL_TARGET_OBJECT");
        FarmingSystem.HarvestPlant(GetPC(), shovel, plant);
        EndConversation();
    }

    @Override
    public void EndDialog() {

        deleteLocalObject(GetPC(), "SHOVEL_ITEM");
        deleteLocalLocation(GetPC(), "SHOVEL_TARGET_LOCATION");
    }
}
