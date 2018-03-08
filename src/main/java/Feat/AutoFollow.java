package Feat;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class AutoFollow implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        final NWObject oTarget = NWScript.getSpellTargetObject();

        if(!NWScript.getIsPC(oTarget) || NWScript.getIsDM(oTarget))
        {
            NWScript.sendMessageToPC(oPC, "You can only follow other players.");
            return;
        }

        NWScript.floatingTextStringOnCreature("Now following " + NWScript.getName(oTarget, false) + ".", oPC, false);
        Scheduler.delay(oPC, 2000, () -> NWScript.actionForceFollowObject(oTarget, 2.0f));
    }
}
