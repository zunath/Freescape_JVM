package Creature;

import Common.IScriptEventHandler;
import GameSystems.SpawnSystem;
import org.nwnx.nwnx2.jvm.NWObject;

public class OnSpawn implements IScriptEventHandler {
    @Override
    public void runScript(NWObject creature) {
        SpawnSystem.OnCreatureSpawn(creature);
    }
}
