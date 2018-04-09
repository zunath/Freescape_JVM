package Placeable.PlantSeed;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import Entities.PCSkillEntity;
import Entities.PlantEntity;
import Enumerations.PerkID;
import Enumerations.SkillID;
import GameSystems.PerkSystem;
import GameSystems.SkillSystem;
import Helper.ItemHelper;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.nwnx.nwnx2.jvm.NWLocation;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.constants.InventoryDisturbType;
import org.nwnx.nwnx2.jvm.constants.ObjectType;

import java.util.concurrent.ThreadLocalRandom;

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

        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(oPC, SkillID.Farming);
        int rank = 0;
        if(pcSkill != null)
        {
            rank = pcSkill.getRank();
        }

        if(rank+2 < plant.getLevel())
        {
            ItemHelper.ReturnItem(oPC, item);
            sendMessageToPC(oPC, "You do not have enough Farming skill to plant that seed. (Required: " + (plant.getLevel() - 2) + ")");
            return;
        }

        destroyObject(item, 0.0f);

        String areaTag = getTag(getArea(planter));
        NWLocation plantLocation = getLocation(planter);
        int perkBonus = PerkSystem.GetPCPerkLevel(oPC, PerkID.FarmingEfficiency) * 2;
        int ticks = (int)(plant.getBaseTicks() - ((PerkSystem.GetPCPerkLevel(oPC, PerkID.ExpertFarmer) * 0.05f)) * plant.getBaseTicks());
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
        growingPlant.setLongevityBonus(perkBonus);

        farmingRepo.Save(growingPlant);


        NWObject hole = getLocalObject(planter, "FARM_SMALL_HOLE");
        NWObject plantPlc = createObject(ObjectType.PLACEABLE, "growing_plant", getLocation(hole), false, "");
        setLocalInt(plantPlc, "GROWING_PLANT_ID", growingPlant.getGrowingPlantID());
        setName(plantPlc, "Growing Plant (" + plant.getName() + ")");

        destroyObject(planter, 0.0f);
        destroyObject(hole, 0.0f);

        int xp = (int)SkillSystem.CalculateSkillAdjustedXP(200, plant.getLevel(), rank);

        if(ThreadLocalRandom.current().nextInt(100) + 1 <= PerkSystem.GetPCPerkLevel(oPC, PerkID.Lucky))
        {
            xp *= 2;
        }

        SkillSystem.GiveSkillXP(oPC, SkillID.Farming, xp);
    }
}
