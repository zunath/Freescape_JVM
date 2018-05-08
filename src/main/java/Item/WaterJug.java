package Item;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Data.Repository.PlayerRepository;
import Entities.GrowingPlantEntity;
import Entities.PCSkillEntity;
import Entities.PlayerEntity;
import Enumerations.BackgroundID;
import Enumerations.SkillID;
import GameObject.PlayerGO;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;

import java.util.concurrent.ThreadLocalRandom;

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class WaterJug implements IScriptEventHandler {
    @Override
    public void runScript(NWObject jug) {
        NWObject oPC = getItemActivator();
        NWObject item = getItemActivated();
        NWObject plant = getItemActivatedTarget();

        int growingPlantID = getLocalInt(plant, "GROWING_PLANT_ID");
        if(growingPlantID <= 0)
        {
            sendMessageToPC(oPC, "Water jugs can only target growing plants.");
            return;
        }

        FarmingRepository farmRepo = new FarmingRepository();
        GrowingPlantEntity growingPlant = farmRepo.GetGrowingPlantByID(growingPlantID);

        if(growingPlant.getWaterStatus() <= 0)
        {
            sendMessageToPC(oPC, "That plant doesn't need to be watered at this time.");
            return;
        }

        int charges = getItemCharges(item);

        if(charges <= 0)
        {
            sendMessageToPC(oPC, "There's no water in that jug!");
            return;
        }

        PlayerRepository playerRepo = new PlayerRepository();
        PlayerGO pcGO = new PlayerGO(oPC);
        PlayerEntity pcEntity = playerRepo.GetByPlayerID(pcGO.getUUID());

        // Farmers get a 5% chance to not expend a charge.
        if (pcEntity.getBackgroundID() != BackgroundID.Farmer || ThreadLocalRandom.current().nextInt(100) + 1 > 5) {
            charges--;
        }

        int remainingTicks = growingPlant.getRemainingTicks();

        if(growingPlant.getWaterStatus() > 1)
        {
            remainingTicks = remainingTicks / 2;
        }

        setItemCharges(item, charges);
        growingPlant.setWaterStatus(0);
        growingPlant.setRemainingTicks(remainingTicks);
        farmRepo.Save(growingPlant);

        sendMessageToPC(oPC, "You water the plant.");


        PCSkillEntity pcSkill = SkillSystem.GetPCSkill(oPC, SkillID.Farming);
        if(pcSkill == null) return;

        int xp = (int)SkillSystem.CalculateSkillAdjustedXP(100, growingPlant.getPlant().getLevel(), pcSkill.getRank());
        SkillSystem.GiveSkillXP(oPC, SkillID.Farming, xp);
    }
}
