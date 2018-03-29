package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {

        boolean hasBeenSearched = getLocalInt(point, "FORAGE_POINT_HAS_BEEN_SEARCHED") == 1;
        if(hasBeenSearched)
        {
            int despawnTicks = getLocalInt(point, "FORAGE_POINT_DESPAWN_TICKS") - 1;

            if(despawnTicks <= 0)
            {
                NWObject[] items = getItemsInInventory(point);
                for(NWObject item: items)
                {
                    destroyObject(item, 0.0f);
                }

                destroyObject(point, 0.0f);
            }
            else setLocalInt(point, "FORAGE_POINT_DESPAWN_TICKS", despawnTicks);

        }

    }
}
