package GameSystems;

import GameObject.PlayerGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;

public class PVPSanctuarySystem {

    public static boolean IsPVPAttackAllowed(NWObject oAttacker, NWObject oTarget)
    {
        PlayerGO attackerGO = new PlayerGO(oAttacker);
        PlayerGO targetGO = new PlayerGO(oTarget);

        // Check for sanctuary if this attack is PC versus PC
        if(NWScript.getIsPC(oTarget) && !NWScript.getIsDMPossessed(oTarget) && NWScript.getIsPC(oAttacker))
        {
            // Either the attacker or target has sanctuary - prevent combat from happening
            if(attackerGO.hasPVPSanctuary())
            {
                NWScript.floatingTextStringOnCreature(ColorToken.Red() + "You are under the effects of PVP sanctuary and cannot engage in PVP. To disable this feature permanently refer to the 'Disable PVP Sanctuary' option in your rest menu." + ColorToken.End(), oAttacker, false);
                Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
                return false;
            }
            else if(targetGO.hasPVPSanctuary())
            {
                NWScript.floatingTextStringOnCreature(ColorToken.Red() + "Your target is under the effects of PVP sanctuary and cannot engage in PVP combat." + ColorToken.End(), oAttacker, false);

                Scheduler.delay(oAttacker, 1, () -> NWScript.clearAllActions(false));
                return false;
            }
        }

        return true;
    }

}
