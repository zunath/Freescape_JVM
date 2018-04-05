package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Enumerations.SkillID;
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
                "Dig a hole"
        );

        dialog.addPage("MainPage", mainPage);
        return dialog;
    }

    @Override
    public void Initialize() {
        NWObject shovel = getLocalObject(GetPC(), "SHOVEL_ITEM");
        int charges = getItemCharges(shovel);

        String header = "This shovel has " + charges + " uses remaining. What would you like to do?";
        SetPageHeader("MainPage", header);
    }

    @Override
    public void DoAction(NWObject oPC, String pageName, int responseID) {
        switch(pageName)
        {
            case "MainPage":
                HandleDigAHole();
                break;
        }
    }

    private void HandleDigAHole()
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

    @Override
    public void EndDialog() {

        deleteLocalObject(GetPC(), "SHOVEL_ITEM");
        deleteLocalLocation(GetPC(), "SHOVEL_TARGET_LOCATION");
    }
}
