package Placeable.Resource;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

// Create a resource prop if it exists on the first heartbeat of the placeable.
public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        boolean hasSpawnedProp = getLocalInt(objSelf, "RESOURCE_PROP_SPAWNED") == 1;
        if(hasSpawnedProp) return;

        String propResref = getLocalString(objSelf, "RESOURCE_PROP");
        if(propResref.equals("")) return;

        NWLocation location = getLocation(objSelf);
        NWObject prop = createObject(ObjectType.PLACEABLE, propResref, location, false, "");
        setLocalObject(objSelf, "RESOURCE_PROP_OBJ", prop);
        setLocalInt(objSelf, "RESOURCE_PROP_SPAWNED", 1);
    }
}
