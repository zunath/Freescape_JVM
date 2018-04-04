package GameSystems;

import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWVector;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.List;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class FarmingSystem {

    public static void OnModuleLoad()
    {
        FarmingRepository farmRepo = new FarmingRepository();
        List<GrowingPlantEntity> plants = farmRepo.GetAllActiveGrowingPlants();

        for(GrowingPlantEntity plant: plants)
        {
            String resref = "growing_plant";
            if(plant.getRemainingTicks() <= 0)
                resref = plant.getPlant().getResref();

            NWObject area = getObjectByTag(plant.getLocationAreaTag(), 0);
            NWVector position = vector(plant.getLocationX(), plant.getLocationY(), plant.getLocationZ());
            NWLocation location = location(area, position, plant.getLocationOrientation());
            NWObject plantPlc = createObject(ObjectType.PLACEABLE, resref, location, false, "");
            setLocalInt(plantPlc, "GROWING_PLANT_ID", plant.getGrowingPlantID());
        }

    }

    public static void RemoveGrowingPlant(NWObject plant)
    {
        int growingPlantID = getLocalInt(plant, "GROWING_PLANT_ID");
        if(growingPlantID <= 0) return;

        FarmingRepository farmRepo = new FarmingRepository();
        GrowingPlantEntity growingPlant = farmRepo.GetGrowingPlantByID(growingPlantID);
        growingPlant.setActive(false);

        farmRepo.Save(growingPlant);
    }

}
