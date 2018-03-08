package Placeable.StructureSystem.PersistentStorage;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        for(NWObject item : NWScript.getItemsInInventory(objSelf))
        {
            NWScript.destroyObject(item, 0.0f);
        }

        NWObject parent = NWScript.getLocalObject(objSelf, "STRUCTURE_TEMP_PARENT");
        NWScript.deleteLocalObject(parent, "STRUCTURE_TEMP_INVENTORY_OPENED");

        NWScript.destroyObject(objSelf, 0.0f);
    }
}
