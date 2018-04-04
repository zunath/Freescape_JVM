package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import GameSystems.FarmingSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {

        boolean isFullyHarvested = getLocalInt(point, "FORAGE_POINT_FULLY_HARVESTED") == 1;

        NWObject[] items = getItemsInInventory(point);
        if(items.length <= 0 && isFullyHarvested)
        {
            String seed = getLocalString(point, "FORAGE_POINT_SEED");
            if(!seed.equals(""))
            {
                createObject(ObjectType.ITEM, seed, getLocation(point), false, "");
            }

            destroyObject(point, 0.0f);
            FarmingSystem.RemoveGrowingPlant(point);
        }
    }
}
