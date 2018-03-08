package Placeable.OverflowStorage;

import Common.IScriptEventHandler;
import Data.Repository.OverflowItemRepository;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

@SuppressWarnings("unused")
public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        final NWObject oPC = NWScript.getLastDisturbed();
        final NWObject oItem = NWScript.getInventoryDisturbItem();
        int type = NWScript.getInventoryDisturbType();

        if(type == InventoryDisturbType.ADDED)
        {
            Scheduler.assign(objSelf, () -> NWScript.actionGiveItem(oItem, oPC));

            return;
        }

        OverflowItemRepository repo = new OverflowItemRepository();
        int overflowItemID = NWScript.getLocalInt(oItem, "TEMP_OVERFLOW_ITEM_ID");
        repo.DeleteByID(overflowItemID);
        NWScript.deleteLocalInt(oItem, "TEMP_OVERFLOW_ITEM_ID");

        if(NWScript.getItemsInInventory(objSelf).length <= 0)
        {
            NWScript.destroyObject(objSelf, 0.0f);
        }
    }
}
