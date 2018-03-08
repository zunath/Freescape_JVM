package Creature;

import Common.IScriptEventHandler;
import GameSystems.CombatSystem;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.*;

public class GasCanister_OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        int iCount = 1;
        NWObject oTarget = NWScript.getNearestObject(ObjectType.CREATURE, objSelf, iCount);
        NWLocation lLocation = NWScript.getLocation(objSelf);
        final NWObject oKiller = NWScript.getLastKiller();

        NWScript.applyEffectAtLocation(DurationType.INSTANT, NWScript.effectVisualEffect(VfxFnf.FIREBALL, false), lLocation, 0.0f);

        while(NWScript.getIsObjectValid(oTarget))
        {
            float fDistance = NWScript.getDistanceBetweenLocations(NWScript.getLocation(oTarget), lLocation);
            if(fDistance > 6.5 || fDistance == 0.0) break;

            final NWObject oTargetCopy = oTarget;
            Scheduler.assign(oKiller, () -> NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectDamage(NWScript.random(10) + 5, DamageType.FIRE, DamagePower.NORMAL), oTargetCopy, 0.0f));

            int iNumberOfTicks = NWScript.random(4) + 1;
            int bCurrentlyOnFire = NWScript.getLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER");
            NWScript.setLocalInt(oTarget, "INCENDIARY_DAMAGE_COUNTER", iNumberOfTicks);
            NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectVisualEffect(VfxDur.INFERNO_CHEST, false), oTarget, (float)(iNumberOfTicks));

            if(bCurrentlyOnFire == 0)
            {
                Scheduler.delay(oKiller, 1000, () -> CombatSystem.IncendiaryDamage(oKiller, oTargetCopy, NWScript.random(3) + 3));
            }

            // Fix the reputation - prevents zombies from bashing gas canisters
            NWScript.adjustReputation(objSelf, oTarget, 100);

            iCount++;
            oTarget = NWScript.getNearestObject(ObjectType.CREATURE, objSelf, iCount);
        }
    }
}
