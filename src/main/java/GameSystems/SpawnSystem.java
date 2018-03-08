package GameSystems;

import Data.Repository.LootTableRepository;
import Data.Repository.SpawnTableRepository;
import Entities.LootTableEntity;
import Entities.LootTableItemEntity;
import Entities.SpawnTableCreatureEntity;
import Entities.SpawnTableEntity;
import GameSystems.Models.SpawnModel;
import Helper.LocalArray;
import Helper.MathHelper;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SpawnSystem {

    // The name of the variable which determines which group to pull spawn resrefs from.
    // This variable is stored on the area object
    private final String SpawnTable = "SPAWN_TABLE";

    // The name of the variable which determines how many spawn points there are in an area
    // This variable is stored on the area object
    private final String SpawnWaypointCount = "ZSS_SPAWN";

    // The name of the variable which determines how many respawn points there are in an area
    // This variable is stored on the area object
    private final String RespawnWaypointCount = "ZSS_RESPAWN";

    // The name of the array which stores spawn waypoint locations
    private final String SpawnWaypointLocationArray = "ZSS_SPAWN_WAYPOINT_LOCATION_ARRAY";

    // The name of the array which stores respawn waypoint locations
    private final String RespawnWaypointLocationArray = "ZSS_RESPAWN_WAYPOINT_LOCATION_ARRAY";

    // Name of the variable which tells the system whether or not a zombie was spawned by the system
    // as opposed to being spawned by a DM.
    private final String IsCreatureSpawned = "ZSS_ZOMBIE_SPAWNED";

    // Name of the variable which tracks whether an area has spawned.
    private final String AreaHasSpawned = "ZSS_AREA_HAS_SPAWNED";

    private void CreateCreature(String creatureResref, NWLocation waypointLocation, String areaResref, int lootTableID, int difficultyRating)
    {
        NWObject oArea = null;

        NWObject area = NWScript.getFirstArea();
        while(NWScript.getIsObjectValid(area))
        {
            if(Objects.equals(NWScript.getResRef(area), areaResref))
            {
                oArea = area;
                break;
            }

            area = NWScript.getNextArea();
        }

        if(oArea == null) return;

        if(NWScript.getLocalInt(oArea, AreaHasSpawned) == 1)
        {
            final NWObject creature = NWScript.createObject(ObjectType.CREATURE, creatureResref, waypointLocation, false, "");
            NWScript.setLocalInt(creature, IsCreatureSpawned, 1);
            CreateLoot(creature, lootTableID);
            NWScript.setLocalInt(creature, "DIFFICULTY_RATING", difficultyRating);

            Scheduler.assign(creature, () -> {
                NWScript.setFacing(0.01f * NWScript.random(3600));
                NWScript.actionRandomWalk();
            });
        }
    }

    public void OnAreaEnter(NWObject oArea)
    {
        NWObject oPC = NWScript.getEnteringObject();

        if(!NWScript.getIsPC(oPC)) return;

        int iZombieCount = NWScript.getLocalInt(oArea, "ZSS_COUNT");
        int iWaypointCount = NWScript.getLocalInt(oArea, SpawnWaypointCount);
        int spawnTableID = NWScript.getLocalInt(oArea, SpawnTable);
        if(spawnTableID < 1) spawnTableID = 1;

        boolean hasSpawned = NWScript.getLocalInt(oArea, AreaHasSpawned) == 1;
        if(!hasSpawned)
        {
            for(int iCurrentZombie = 1; iCurrentZombie <= iZombieCount; iCurrentZombie++)
            {
                int iWaypoint;

                // Ensure that at least one zombie will be placed at each spawn point
                if(iCurrentZombie > iWaypointCount)
                {
                    iWaypoint = NWScript.random(iWaypointCount) + 1;
                }
                else
                {
                    iWaypoint = iCurrentZombie;
                }

                SpawnModel model = GetCreatureToSpawn(spawnTableID);
                NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, SpawnWaypointLocationArray, iWaypoint);

                final NWObject creature = NWScript.createObject(ObjectType.CREATURE, model.getResref(), lLocation, false, "");
                NWScript.setLocalInt(creature, IsCreatureSpawned, 1);
                CreateLoot(creature, model.getLootTableID());
                NWScript.setLocalInt(creature, "DIFFICULTY_RATING", model.getDifficultyRating());

                Scheduler.assign(creature, () -> NWScript.setFacing(0.01f * NWScript.random(3600)));
            }

            NWScript.setLocalInt(oArea, AreaHasSpawned, 1);
        }
    }

    public void OnModuleHeartbeat()
    {
        final int MaxDespawnTicks = 1800 / 6; // (1800 seconds / 6 seconds per tick = Despawn after 30 minutes
        HashSet<String> pcAreas = new HashSet<>();

        for(NWObject pc : NWScript.getPCs())
        {
            String areaResref = NWScript.getResRef(NWScript.getArea(pc));
            if(!pcAreas.contains(areaResref))
                pcAreas.add(areaResref);
        }

        NWObject area = NWScript.getFirstArea();
        while(NWScript.getIsObjectValid(area))
        {
            String areaResref = NWScript.getResRef(area);

            if(NWScript.getLocalInt(area, AreaHasSpawned) == 1)
            {
                int currentDespawnTicks = 0;
                String currentAreaDespawnTick = "ZSS_CURRENT_DESPAWN_TICK";
                if(!pcAreas.contains(areaResref))
                {
                    currentDespawnTicks = NWScript.getLocalInt(area, currentAreaDespawnTick) + 1;

                    if(currentDespawnTicks >= MaxDespawnTicks)
                    {
                        ClearSpawns(area);
                        currentDespawnTicks = 0;
                        NWScript.deleteLocalInt(area, AreaHasSpawned);
                    }
                }

                NWScript.setLocalInt(area, currentAreaDespawnTick, currentDespawnTicks);
            }


            area = NWScript.getNextArea();
        }
    }

    private void ClearSpawns(NWObject oArea)
    {
        NWObject[] objects = NWScript.getObjectsInArea(oArea);

        for(NWObject currentObject : objects)
        {
            if(NWScript.getLocalInt(currentObject, IsCreatureSpawned) == 1)
            {
                NWScript.destroyObject(currentObject, 0.0f);
            }
        }
    }

    public void OnModuleLoad()
    {
        NWObject oArea = NWScript.getFirstArea();

        while(NWScript.getIsObjectValid(oArea))
        {
            String sSpawnID = NWScript.getResRef(oArea);
            int iSpawnCount = 0;
            int iRespawnCount = 0;

            NWObject[] areaObjects = NWScript.getObjectsInArea(oArea);
            for(NWObject obj : areaObjects)
            {
                String sResref = NWScript.getResRef(obj);
                if(Objects.equals(sResref, "zombie_spawn"))
                {
                    iSpawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, SpawnWaypointLocationArray, iSpawnCount, NWScript.getLocation(obj));
                }
                // Respawn waypoint
                else if(Objects.equals(sResref, "zombie_respawn"))
                {
                    iRespawnCount++;
                    LocalArray.SetLocalArrayLocation(oArea, RespawnWaypointLocationArray, iRespawnCount, NWScript.getLocation(obj));
                }
            }

            // Mark the number of spawn and respawn waypoints for this area
            NWScript.setLocalInt(oArea, SpawnWaypointCount, iSpawnCount);
            NWScript.setLocalInt(oArea, RespawnWaypointCount, iRespawnCount);
            // Mark the unique identifier (the resref)
            NWScript.setLocalString(oArea, "ZSS_WAYPOINT_NAME", sSpawnID);

            oArea = NWScript.getNextArea();
        }
    }

    public void OnCreatureDeath(NWObject creature)
    {
        final NWObject oArea = NWScript.getArea(creature);
        final String areaResref = NWScript.getResRef(oArea);
        int iWaypointCount = NWScript.getLocalInt(oArea, RespawnWaypointCount);

        if(NWScript.getLocalInt(creature, IsCreatureSpawned) != 1 || iWaypointCount <= 0) return;

        int iGroupID = NWScript.getLocalInt(oArea, SpawnTable);
        if(iGroupID <= 0) iGroupID = 1;

        if(NWScript.getLocalInt(oArea, AreaHasSpawned) == 1)
        {
            int iWaypoint = NWScript.random(iWaypointCount) + 1;
            final SpawnModel model = GetCreatureToSpawn(iGroupID);
            final NWLocation lLocation = LocalArray.GetLocalArrayLocation(oArea, RespawnWaypointLocationArray, iWaypoint);

            // 120 * 1000 = 2 Minutes
            Scheduler.delay(creature, 120 * 1000, () -> CreateCreature(model.getResref(), lLocation, areaResref, model.getLootTableID(), model.getDifficultyRating()));

        }
    }


    private SpawnModel GetCreatureToSpawn(int spawnTableID)
    {
        SpawnTableRepository repo = new SpawnTableRepository();
        SpawnTableEntity entity = repo.GetBySpawnTableID(spawnTableID);
        int weights[] = new int[entity.getSpawnTableCreatures().size()];

        for(int x = 0; x < entity.getSpawnTableCreatures().size(); x++)
        {
            weights[x] = entity.getSpawnTableCreatures().get(x).getWeight();
        }

        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);
        SpawnTableCreatureEntity creatureEntity = entity.getSpawnTableCreatures().get(randomIndex);

        return new SpawnModel(creatureEntity.getResref(), creatureEntity.getLootTableID(), creatureEntity.getDifficultyRating());
    }

    private void CreateLoot(NWObject creature, Integer lootTableID)
    {
        if(lootTableID == null || !NWScript.getIsObjectValid(creature)) return;
        if(ThreadLocalRandom.current().nextInt(0, 100) > 20) return;

        LootTableRepository repo = new LootTableRepository();
        LootTableEntity entity = repo.GetByLootTableID(lootTableID);
        int[] weights = new int[entity.getLootTableItems().size()];

        for(int x = 0; x < entity.getLootTableItems().size(); x++)
        {
            weights[x] = entity.getLootTableItems().get(x).getWeight();
        }

        int randomIndex = MathHelper.GetRandomWeightedIndex(weights);
        LootTableItemEntity itemEntity = entity.getLootTableItems().get(randomIndex);
        int quantity =
                itemEntity.getMaxQuantity() == 1 ? 1 :
                ThreadLocalRandom.current().nextInt(1, itemEntity.getMaxQuantity());

        if(!itemEntity.getResref().equals("") && quantity > 0)
        {
            NWObject item = NWScript.createItemOnObject(itemEntity.getResref(), creature, quantity, "");
            int maxDurability = DurabilitySystem.GetMaxItemDurability(item);

            if(maxDurability > -1)
                DurabilitySystem.SetItemDurability(item, ThreadLocalRandom.current().nextInt(1, maxDurability));
        }
    }

}
