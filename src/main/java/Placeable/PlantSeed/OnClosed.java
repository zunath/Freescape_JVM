package Placeable.PlantSeed;

import Common.IScriptEventHandler;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject planter) {
        NWObject oPC = getLastClosedBy();

        NWObject[] items = getItemsInInventory(planter);
        for(NWObject item: items)
        {
            ItemHelper.ReturnItem(oPC, item);
        }

        destroyObject(planter, 0.0f);
    }
}
