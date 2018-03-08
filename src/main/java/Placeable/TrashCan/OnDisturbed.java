package Placeable.TrashCan;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;

public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oItem = NWScript.getInventoryDisturbItem();
        int type = NWScript.getInventoryDisturbType();

        if(type == InventoryDisturbType.ADDED)
        {
            NWScript.destroyObject(oItem, 0.0f);
        }
    }
}
