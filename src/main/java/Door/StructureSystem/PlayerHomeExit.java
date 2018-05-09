package Door.StructureSystem;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.Scheduler;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class PlayerHomeExit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject door) {
        NWObject oPC = getClickingObject();
        NWObject oArea = getArea(door);
        NWLocation location = getLocalLocation(door, "PLAYER_HOME_EXIT_LOCATION");

        Scheduler.assignNow(oPC, () -> actionJumpToLocation(location));
        Scheduler.delay(door, 1000, () -> destroyArea(oArea));
    }
}
