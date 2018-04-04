package Placeable.GrowingPlant;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnHeartbeat implements IScriptEventHandler {
    @Override
    public void runScript(NWObject plant) {
        int growingPlantID = getLocalInt(plant, "GROWING_PLANT_ID");
        if(growingPlantID <= 0) return;

        FarmingRepository farmRepo = new FarmingRepository();
        GrowingPlantEntity growingPlant = farmRepo.GetGrowingPlantByID(growingPlantID);
        growingPlant.setRemainingTicks(growingPlant.getRemainingTicks()-1);

        if(growingPlant.getRemainingTicks() <= 0)
        {
            destroyObject(plant, 0.0f);
            plant = createObject(ObjectType.PLACEABLE, growingPlant.getPlant().getResref(), getLocation(plant), false, "");
            setLocalInt(plant, "GROWING_PLANT_ID", growingPlantID);
        }

        farmRepo.Save(growingPlant);
    }
}
