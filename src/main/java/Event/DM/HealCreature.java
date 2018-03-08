package Event.DM;

import Common.IScriptEventHandler;
import org.nwnx.nwnx2.jvm.NWObject;

@SuppressWarnings("unused")
public class HealCreature implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        // TODO: Update for EE
        //NWNX_Events_Old.BypassEvent();
        //NWObject oTarget = NWNX_Events_Old.GetEventTarget();
        //int health = NWScript.getMaxHitPoints(oTarget);
        //NWEffect effect = NWScript.effectHeal(health);
        //NWScript.applyEffectToObject(DurationType.INSTANT, effect, oTarget, 0.0f);

    }
}
