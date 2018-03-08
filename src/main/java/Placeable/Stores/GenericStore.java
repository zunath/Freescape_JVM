package Placeable.Stores;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class GenericStore implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();
        String storeTag = NWScript.getLocalString(objSelf, "STORE_TAG");
        NWObject store = NWScript.getObjectByTag(storeTag, 0);

        NWScript.openStore(store, oPC, 0, 0);
    }
}
