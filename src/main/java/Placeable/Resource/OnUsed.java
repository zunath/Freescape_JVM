package Placeable.Resource;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oPC = NWScript.getLastUsedBy();

        Scheduler.assign(oPC, () -> NWScript.actionAttack(objSelf, false));
    }
}
