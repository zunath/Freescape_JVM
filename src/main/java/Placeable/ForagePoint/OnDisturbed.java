package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = getLastDisturbed();
        NWObject oItem = getInventoryDisturbItem();
        int disturbType = getInventoryDisturbType();

        if(disturbType == InventoryDisturbType.ADDED)
        {
            ItemHelper.ReturnItem(oPC, oItem);
        }
        else
        {
            NWObject[] items = getItemsInInventory(objSelf);
            if(items.length <= 0)
            {
                destroyObject(objSelf, 0.0f);
            }
        }
    }
}
