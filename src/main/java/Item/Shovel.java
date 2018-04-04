package Item;

import Common.IScriptEventHandler;
import Dialog.DialogManager;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class Shovel implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject oItem = getItemActivated();
        NWLocation targetLocation = getItemActivatedTargetLocation();
        NWObject area = getArea(oPC);
        boolean farmingDisabled = getLocalInt(area, "FARMING_DISABLED") == 1;

        if(farmingDisabled)
        {
            sendMessageToPC(oPC, "You cannot dig a hole in this area.");
            return;
        }

        setLocalObject(oPC, "SHOVEL_ITEM", oItem);
        setLocalLocation(oPC, "SHOVEL_TARGET_LOCATION", targetLocation);
        Scheduler.assignNow(oPC, () -> NWScript.clearAllActions(false));
        DialogManager.startConversation(oPC, oPC, "Shovel");
    }
}
