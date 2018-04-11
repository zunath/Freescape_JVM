package Placeable;

import Common.IScriptEventHandler;
import NWNX.NWNX_Item;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class TestBread implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = getLastUsedBy();
        NWObject item = getItemInSlot(InventorySlot.LEFTHAND, oPC);

        sendMessageToPC(oPC, "Armor Value before set = " + NWNX_Item.GetArmorValue(item));
        NWNX_Item.SetArmorValue(item, 20);
        sendMessageToPC(oPC, "Armor Value After set = " + NWNX_Item.GetArmorValue(item));
    }
}
