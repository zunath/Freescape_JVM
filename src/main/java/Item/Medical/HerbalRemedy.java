package Item.Medical;

import Entities.PlayerEntity;
import GameObject.PlayerGO;
import Helper.ItemHelper;
import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import GameSystems.DiseaseSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;

@SuppressWarnings("unused")
public class HerbalRemedy implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        if(!NWScript.getIsPC(oPC) || NWScript.getIsDM(oPC)) return;

        PlayerGO pcGO = new PlayerGO(oPC);
        NWObject oItem = NWScript.getItemActivated();
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(pcGO.getUUID());
        int skillLevel = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);

        if(entity.getCurrentInfection() <= 0)
        {
            NWScript.sendMessageToPC(oPC, "You are not infected. There would be no point to use this item.");
            return;
        }

        Scheduler.assign(oPC, () -> NWScript.actionPlayAnimation(Animation.FIREFORGET_SALUTE, 1.0f, 0.0f));

        DiseaseSystem.DecreaseDiseaseLevel(oPC, NWScript.random(10) + 1 + NWScript.random(skillLevel * 2));
        ItemHelper.ReduceItemStack(oItem);
    }
}
