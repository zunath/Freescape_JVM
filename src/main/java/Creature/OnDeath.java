package Creature;

import Common.IScriptEventHandler;
import GameSystems.LootSystem;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnDeath implements IScriptEventHandler {
    @Override
    public void runScript(NWObject creature) {
        SkillSystem.OnCreatureDeath(creature);
        LootSystem.OnCreatureDeath(creature);
    }
}
