package Placeable.Resource;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.EventScript;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

// Create a resource prop if it exists on the first heartbeat of the placeable.
// Once run, the heartbeat will be removed from the object.
public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        setEventScript(objSelf, EventScript.CREATURE_ON_HEARTBEAT, "");


        String propResref = getLocalString(objSelf, "RESOURCE_PROP");
        if(propResref.equals("")) return;

        NWLocation location = getLocation(objSelf);
        NWObject prop = createObject(ObjectType.PLACEABLE, propResref, location, false, getResRef(objSelf));
        setLocalObject(objSelf, "RESOURCE_PROP", prop);
    }
}
