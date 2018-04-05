package Item;

import Common.IScriptEventHandler;
import Data.Repository.FarmingRepository;
import Entities.GrowingPlantEntity;
import Entities.PCSkillEntity;
import Enumerations.SkillID;
import GameSystems.SkillSystem;
import org.nwnx.nwnx2.jvm.NWObject;
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

        int charges = getItemCharges(item) - 1;

        if(charges <= 0)
        {
            sendMessageToPC(oPC, "There's no water in that jug!");
            return;
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
