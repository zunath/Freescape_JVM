package Placeable.TrashCan;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnClosed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject[] items = getItemsInInventory(objSelf);
        for(NWObject item: items)
        {
            destroyObject(item, 0.0f);
        }

        destroyObject(objSelf, 0.0f);
    }
}
