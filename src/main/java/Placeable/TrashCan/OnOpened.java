package Placeable.TrashCan;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class OnOpened implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastOpenedBy();
        NWScript.floatingTextStringOnCreature("Any item placed inside this trash can will be destroyed permanently.", oPC, false);
    }
}
