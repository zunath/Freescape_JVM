package GameSystems;

import Data.Repository.FarmingRepository;
import Entities.PlantEntity;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class SpawnSystem {

    private static class QueuedObject {
        private String resref;
        private NWLocation location;
        private int ticks;

        public String getResref() {
            return resref;
        }

        public void setResref(String resref) {
            this.resref = resref;
        }

        public NWLocation getLocation() {
            return location;
        }

        public void setLocation(NWLocation location) {
            this.location = location;
        }

        public int getTicks() {
            return ticks;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }
    }

    private static HashMap<String, QueuedObject> RespawnQueue = new HashMap<>();

    public static void AddToRespawnQueue(NWObject target)
    {
        if(getObjectType(target) != ObjectType.PLACEABLE) return;

        String resref = getResRef(target);
        FarmingRepository farmRepo = new FarmingRepository();

        // Anything which can be planted isn't eligible to respawn.
        List<PlantEntity> plants = farmRepo.GetPlantsByResref(resref);
        if(plants.size() > 0) return;

        QueuedObject obj = new QueuedObject();
        obj.setTicks(3600); // 3600 = 6 hours
        obj.setLocation(getLocation(target));
        obj.setResref(resref);

        RespawnQueue.put(UUID.randomUUID().toString(), obj);
    }

    public static void OnModuleHeartbeat()
    {
        for(Map.Entry<String, QueuedObject> obj: RespawnQueue.entrySet())
        {
            obj.getValue().setTicks(obj.getValue().getTicks()-1);

            if(obj.getValue().getTicks() <= 0)
            {
                createObject(ObjectType.PLACEABLE, obj.getValue().getResref(), obj.getValue().getLocation(), false, "");
                RespawnQueue.remove(obj.getKey());
            }
            else
            {
                RespawnQueue.put(obj.getKey(), obj.getValue());
            }

        }
    }

}
