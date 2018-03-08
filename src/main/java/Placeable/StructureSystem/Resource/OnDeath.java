package Placeable.StructureSystem.Resource;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

public class OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWLocation location = NWScript.getLocation(objSelf);
        String resourceItemResref = NWScript.getLocalString(objSelf, "RESOURCE_RESREF");

        NWScript.createObject(ObjectType.ITEM, resourceItemResref, location, false, "");
    }
}
