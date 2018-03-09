package Creature;

import Common.IScriptEventHandler;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject creature) {
        SkillSystem.OnCreatureDeath(creature);
    }
}
