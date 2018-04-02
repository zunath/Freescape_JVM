package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject point) {

        boolean isFullyHarvested = getLocalInt(point, "FORAGE_POINT_FULLY_HARVESTED") == 1;
        if(isFullyHarvested)
        {
            int despawnTicks = getLocalInt(point, "FORAGE_POINT_REFILL_TICKS") - 1;
            if(despawnTicks <= 0) despawnTicks = 0;

            setLocalInt(point, "FORAGE_POINT_REFILL_TICKS", despawnTicks);
        }

    }
}
