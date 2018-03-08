package Conversation;

import Dialog.DialogBase;
import Dialog.DialogPage;
import Dialog.IDialogHandler;
import Dialog.PlayerDialog;
import Helper.ColorToken;
import GameSystems.StructureSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

@SuppressWarnings("unused")
public class StructureStorage extends DialogBase implements IDialogHandler {
    @Override
    public PlayerDialog SetUp(NWObject oPC) {
        PlayerDialog dialog = new PlayerDialog("MainPage");
        DialogPage mainPage = new DialogPage(
                ColorToken.Green() + "Persistent Storage Menu" + ColorToken.End() + "\n\nPlease select an option.",
                "Open Storage"
        );

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
                OpenPersistentStorage();
                EndConversation();
                break;
        }

    }

    @Override
    public void EndDialog() {

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
