package Creature;

import Common.IScriptEventHandler;
import GameSystems.CombatSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("UnusedDeclaration")
public class NPC_CombatRoutine implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {
        NWObject oTarget = NWScript.getLocalObject(objSelf, "COMBAT_TEMP_TARGET");
        CombatSystem combatSystem = new CombatSystem();
        boolean runDefault = combatSystem.NPCCombatRoutine(objSelf, oTarget);

        NWScript.deleteLocalObject(objSelf, "COMBAT_TEMP_TARGET");
        NWScript.setLocalInt(objSelf, "COMBAT_RUN_DEFAULT_SCRIPT", runDefault ? 1 : 0);
    }
}
