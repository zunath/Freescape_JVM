package GameSystems;

import Data.Repository.SpawnRepository;
import Entities.CreatureEntity;
import GameObject.CreatureGO;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.UUID;

public class SpawnSystem {
    public static void OnCreatureSpawn(NWObject creature)
    {
        SpawnRepository repo = new SpawnRepository();
        CreatureGO creatureGO = new CreatureGO(creature);
        CreatureEntity creatureEntity = repo.GetCreatureByID(creatureGO.getCreatureID());
        if(creatureEntity == null) return;

        creatureGO.setGlobalUUID(UUID.randomUUID().toString());
        creatureGO.setDifficultyRating(creatureEntity.getDifficultyRating());
        creatureGO.setXPModifier(creatureEntity.getXPModifier());
    }
}
