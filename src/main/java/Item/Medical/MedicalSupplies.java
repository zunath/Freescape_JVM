package Item.Medical;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameObject.PlayerGO;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import Helper.ItemHelper;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;

@SuppressWarnings("unused")
public class MedicalSupplies implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {

        NWObject oTarget = NWScript.getItemActivatedTarget();
        final PlayerGO pcGO = new PlayerGO(oPC);

        if(pcGO.isBusy())
        {
            NWScript.sendMessageToPC(oPC, "You are busy.");
            return;
        }

        if(!NWScript.getIsPC(oTarget) || NWScript.getIsDM(oTarget))
        {
            NWScript.sendMessageToPC(oPC, "Only players may be targeted with this item.");
            return;
        }

        if(NWScript.getCurrentHitPoints(oTarget) >= NWScript.getMaxHitPoints(oTarget))
        {
            NWScript.sendMessageToPC(oPC, "Your target is not hurt.");
            return;
        }

        float distance = NWScript.getDistanceBetween(oPC, oTarget);
        if(distance > 3.5f)
        {
            NWScript.sendMessageToPC(oPC, "Your target is out of range.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);
        float duration = 30.0f + (skill * 6.0f);
        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Medic))
        {
            duration *= 1.25f;
        }

        final float finalDuration = duration;
        final float delay = 12.0f - (skill * 0.5f);


        final NWObject item = NWScript.getItemActivated();
        final int restoreAmount = 1 + NWScript.getLocalInt(item, "ENHANCED_AMOUNT");

        NWNX_Player.StartGuiTimingBar(oPC, (int) delay, "");
        NWScript.sendMessageToPC(oPC, "You begin treating " + NWScript.getName(oTarget, false) + "'s wounds.");
        if(!oPC.equals(oTarget))
            NWScript.sendMessageToPC(oTarget, NWScript.getName(oPC, false) + " begins treating your wounds.");

        Scheduler.assign(oPC, () -> {
            pcGO.setIsBusy(true);
            NWScript.setFacingPoint(NWScript.getPosition(oTarget));
            NWScript.actionPlayAnimation(Animation.LOOPING_GET_MID, 1.0f, delay);
            NWScript.setCommandable(false, oPC);
        });

        Scheduler.delay(oPC, (int) (1000 * delay), () -> {
            pcGO.setIsBusy(false);
            NWScript.setCommandable(true, oPC);
            float distance1 = NWScript.getDistanceBetween(oPC, oTarget);

            if(distance1 > 3.5f)
            {
                NWScript.sendMessageToPC(oPC, "Your target is too far away.");
                return;
            }
            PlayerGO targetGO = new PlayerGO(oTarget);
            targetGO.removeEffect(EffectType.REGENERATE);

            NWEffect regeneration = NWScript.effectRegenerate(restoreAmount, 6.0f);
            NWScript.applyEffectToObject(DurationType.TEMPORARY, regeneration, oTarget, finalDuration);
            ItemHelper.ReduceItemStack(item);

            NWScript.sendMessageToPC(oPC, "You successfully treat " + NWScript.getName(oTarget, false) + "'s wounds.");

        });
    }
}
