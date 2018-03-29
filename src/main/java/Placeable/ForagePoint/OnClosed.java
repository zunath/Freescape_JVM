package Placeable.ForagePoint;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.destroyObject;
import static org.nwnx.nwnx2.jvm.NWScript.getItemsInInventory;

public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject[] items = getItemsInInventory(objSelf);

        if(items.length <= 0)
        {
            destroyObject(objSelf, 0.0f);
        }
    }
}
