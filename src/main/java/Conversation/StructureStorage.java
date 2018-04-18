package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Helper.ColorToken;
import GameSystems.StructureSystem;
import com.sun.xml.internal.bind.v2.runtime.Name;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.NavigableMap;

import static org.nwnx.nwnx2.jvm.NWScript.*;

@SuppressWarnings("unused")
public class StructureStorage extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                ColorToken.Green() + "Persistent Storage Menu" + ColorToken.End() + "\n\nPlease select an option.",
                "Open Storage",
                "Change Container Name"
        );

        DialogPage changeNamePage = new DialogPage(
                ColorToken.Green() + "Change Container Name" + ColorToken.End() + "\n\nPlease type a name for the container into your chat bar and then press enter. After that's done click the 'Next' button on this conversation window.",
                "Next",
                "Back"
        );

        DialogPage confirmChangeName = new DialogPage(
                "<SET LATER>",
                "Confirm Name Change",
                "Back"
        );

        dialog.addPage("MainPage", mainPage);
        dialog.addPage("ChangeNamePage", changeNamePage);
        dialog.addPage("ConfirmChangeNamePage", confirmChangeName);
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
                HandleMainPageResponse(responseID);
                break;
            case "ChangeNamePage":
                HandleChangeNamePageResponse(responseID);
                break;
            case "ConfirmChangeNamePage":
                HandleConfirmChangeNamePageResponse(responseID);
                break;
        }

    }

    private void HandleMainPageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Open Storage
                OpenPersistentStorage();
                EndConversation();
                break;
            case 2: // Change Container Name
                setLocalInt(GetPC(), "LISTENING_FOR_NEW_CONTAINER_NAME", 1);
                ChangePage("ChangeNamePage");
                break;
        }
    }

    private void HandleChangeNamePageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Next
                String name = getLocalString(GetPC(), "NEW_CONTAINER_NAME");
                if(name.equals(""))
                {
                    floatingTextStringOnCreature("Type in a new name to the chat bar and then press 'Next'.", GetPC(), false);
                    return;
                }

                String header = ColorToken.Green() + "Change Container Name" + ColorToken.End() + "\n\n";
                header += ColorToken.Green() + "New Container Name: " + ColorToken.End() + name + "\n\n";
                header += "Are you sure you want to change your container to this name?";

                SetPageHeader("ConfirmChangeNamePage", header);
                ChangePage("ConfirmChangeNamePage");
                break;
            case 2: // Back
                ClearTempVariables();
                ChangePage("MainPage");
                break;
        }
    }

    private void HandleConfirmChangeNamePageResponse(int responseID)
    {
        switch (responseID)
        {
            case 1: // Confirm Change Name
                String name = getLocalString(GetPC(), "NEW_CONTAINER_NAME");
                StructureSystem.SetStructureCustomName(GetDialogTarget(), name);
                EndConversation();
                floatingTextStringOnCreature("New name set: " + name, GetPC(), false);
                break;
            case 2: // Back
                ChangePage("ChangeNamePage");
                break;
        }
    }

    private void ClearTempVariables()
    {
        deleteLocalInt(GetPC(), "LISTENING_FOR_NEW_CONTAINER_NAME");
        deleteLocalString(GetPC(), "NEW_CONTAINER_NAME");
    }

    @Override
    public void EndDialog() {
        ClearTempVariables();
    }

    private void OpenPersistentStorage()
    {
        NWObject chest = GetDialogTarget();
        final NWObject oPC = GetPC();

        if(!NWScript.getLocalObject(chest, "STRUCTURE_TEMP_INVENTORY_OPENED").equals(NWObject.INVALID))
        {
            NWScript.floatingTextStringOnCreature("Someone else is already accessing that structure's inventory. Please wait.", oPC, false);
            return;
        }

        int structureID = StructureSystem.GetPlaceableStructureID(chest);
        NWLocation location = NWScript.getLocation(oPC);
        final NWObject copy = NWScript.createObject(ObjectType.PLACEABLE, "str_storage_copy", location, false, "");
        NWScript.setName(copy, NWScript.getName(chest, false));

        Scheduler.assign(copy, () -> NWScript.setFacingPoint(NWScript.getPosition(oPC)));
        NWScript.setLocalObject(chest, "STRUCTURE_TEMP_INVENTORY_OPENED", copy);
        NWScript.setLocalObject(copy, "STRUCTURE_TEMP_PARENT", chest);
        NWScript.setLocalInt(copy, "STRUCTURE_TEMP_STRUCTURE_ID", structureID);

        Scheduler.assign(oPC, () -> NWScript.actionInteractObject(copy));
    }

}
