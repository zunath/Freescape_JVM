package GameSystems;

import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class ObjectProcessingSystem {

    public static void OnModuleLoad()
    {
        NWObject area = getFirstArea();
        while(getIsObjectValid(area))
        {
            NWObject[] objects = getObjectsInArea(area);
            for(NWObject obj: objects)
            {
                HandleSpawnWaypointRename(obj);
            }

            area = getNextArea();
        }
    }

    // It's difficult to see what waypoint represents what in the toolset.
    // To fix this, we rename the waypoints on module load so that they function in-game.
    private static void HandleSpawnWaypointRename(NWObject obj)
    {
        if(getObjectType(obj) != ObjectType.WAYPOINT) return;

        String name = getName(obj, false);

        String[] split = name.split("SP_");

        if(split.length <= 1) return;

        name = "SP_" + split[split.length - 1];
        name = name.trim();

        setName(obj, name);
    }
}
