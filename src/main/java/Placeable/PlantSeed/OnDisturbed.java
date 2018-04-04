package Placeable.PlantSeed;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import Entities.PlantEntity;
import Helper.ItemHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.sql.Timestamp;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class OnDisturbed implements IScriptEventHandler {
    @Override
    public void runScript(NWObject planter) {
        NWObject oPC = getLastDisturbed();
        int type = getInventoryDisturbType();
        NWObject item = getInventoryDisturbItem();

        if(type != InventoryDisturbType.ADDED) return;

        int plantID = getLocalInt(item, "PLANT_ID");

        if(plantID <= 0)
        {
            ItemHelper.ReturnItem(oPC, item);
            sendMessageToPC(oPC, "You cannot plant that item.");
            return;
        }

        FarmingRepository farmingRepo = new FarmingRepository();
        PlantEntity plant = farmingRepo.GetPlantByID(plantID);
        if(plant == null)
        {
            ItemHelper.ReturnItem(oPC, item);
            sendMessageToPC(oPC, "You cannot plant that item.");
            return;
        }

        destroyObject(item, 0.0f);

        String areaTag = getTag(getArea(planter));
        NWLocation plantLocation = getLocation(planter);
        int ticks = plant.getBaseTicks();
        GrowingPlantEntity growingPlant = new GrowingPlantEntity();
        growingPlant.setPlant(plant);
        growingPlant.setRemainingTicks(ticks);
        growingPlant.setLocationAreaTag(areaTag);
        growingPlant.setLocationOrientation(plantLocation.getFacing());
        growingPlant.setLocationX(plantLocation.getX());
        growingPlant.setLocationY(plantLocation.getY());
        growingPlant.setLocationZ(plantLocation.getZ());
        growingPlant.setActive(true);
        growingPlant.setDateCreated(new DateTime(DateTimeZone.UTC).toDate());

        farmingRepo.Save(growingPlant);


        NWObject hole = getLocalObject(planter, "FARM_SMALL_HOLE");
        NWObject plantPlc = createObject(ObjectType.PLACEABLE, "growing_plant", getLocation(hole), false, "");
        setLocalInt(plantPlc, "GROWING_PLANT_ID", growingPlant.getGrowingPlantID());
        setName(plantPlc, "Growing Plant (" + plant.getName() + ")");

        destroyObject(planter, 0.0f);
        destroyObject(hole, 0.0f);
    }
}
