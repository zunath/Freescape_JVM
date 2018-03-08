package Item.Medical;

import Common.IScriptEventHandler;
import Data.Repository.PlayerRepository;
import Entities.PlayerEntity;
import GameObject.PlayerGO;
import GameSystems.DiseaseSystem;
import GameSystems.ProgressionSystem;
import Helper.ItemHelper;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;

import java.util.Random;

@SuppressWarnings("unused")
public class TreatmentKit implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {
        NWObject target = NWScript.getItemActivatedTarget();
        final PlayerGO pcGO = new PlayerGO(oPC);

        if(pcGO.isBusy())
        {
            NWScript.sendMessageToPC(oPC, "You are busy.");
            return;
        }

        PlayerGO targetGO = new PlayerGO(target);
        NWObject oItem = NWScript.getItemActivated();
        PlayerRepository repo = new PlayerRepository();
        PlayerEntity entity = repo.GetByPlayerID(targetGO.getUUID());

        if(!NWScript.getIsPC(target) || NWScript.getIsDM(target) || entity.getCurrentInfection() <= 0)
        {
            NWScript.sendMessageToPC(oPC, "Only infected players may be targeted with this item.");
            return;
        }

        float distance = NWScript.getDistanceBetween(oPC, target);
        if(distance > 3.5f)
        {
            NWScript.sendMessageToPC(oPC, "Your target is too far away.");
            return;
        }

        Random random = new Random();
        final NWObject item = NWScript.getItemActivated();
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);
        int skillBonus = skill / 2;
        final float delay = 8.0f - (skill * 0.5f);
        int baseAmount = 4 + NWScript.getLocalInt(item, "ENHANCED_AMOUNT") + (skillBonus);
        final int restoreAmount = baseAmount + random.nextInt(3);

        NWNX_Player.StartGuiTimingBar(oPC, (int) delay, "");

        Scheduler.assign(oPC, () -> {
            pcGO.setIsBusy(true);
            NWScript.setFacingPoint(NWScript.getPosition(target));
            NWScript.actionPlayAnimation(Animation.LOOPING_GET_MID, 1.0f, delay);
            NWScript.setCommandable(false, oPC);
        });

        Scheduler.delay(oPC, (int) (delay * 1000), () -> {
            pcGO.setIsBusy(false);
            NWScript.setCommandable(true, oPC);
            float distance1 = NWScript.getDistanceBetween(oPC, target);

            if (distance1 > 3.5f) {
                NWScript.sendMessageToPC(oPC, "Your target is too far away.");
                return;
            }

            DiseaseSystem.DecreaseDiseaseLevel(target, restoreAmount);
            ItemHelper.ReduceItemStack(item);
        });
    }
}
