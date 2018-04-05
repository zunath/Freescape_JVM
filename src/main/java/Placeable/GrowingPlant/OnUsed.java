package Placeable.GrowingPlant;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import org.nwnx.nwnx2.jvm.NWObject;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnUsed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject plant) {
        int growingPlantID = getLocalInt(plant, "GROWING_PLANT_ID");
        if(growingPlantID <= 0) return;

        NWObject oPC = getLastUsedBy();
        FarmingRepository farmRepo = new FarmingRepository();
        GrowingPlantEntity growingPlant = farmRepo.GetGrowingPlantByID(growingPlantID);
        if(growingPlant.getWaterStatus() <= 0)
        {
            sendMessageToPC(oPC, "This plant doesn't seem to need anything right now.");
            return;
        }

        sendMessageToPC(oPC, "This plant needs to be watered. Use a Water Jug on it to water it. These can be crafted with the Metalworking skill.");
    }
}
