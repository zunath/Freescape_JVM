package GameSystems;

import GameObject.CreatureGO;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.UUID;

public class SpawnSystem {
    public static void OnCreatureSpawn(NWObject creature)
    {
        CreatureGO creatureGO = new CreatureGO(creature);
        creatureGO.setUUID(UUID.randomUUID().toString());
    }
}
