package Item.Medical;

import Common.IScriptEventHandler;
import Enumerations.CustomEffectType;
import GameObject.PlayerGO;
import GameSystems.CustomEffectSystem;
import GameSystems.ProgressionSystem;
import Helper.ItemHelper;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;

@SuppressWarnings("unused")
public class Antibiotics implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {
        final PlayerGO pcGO = new PlayerGO(oPC);
        NWObject target = NWScript.getItemActivatedTarget();

        if(pcGO.isBusy())
        {
            NWScript.sendMessageToPC(oPC, "You are busy.");
            return;
        }

        if(!NWScript.getIsPC(target) || NWScript.getIsDM(target))
        {
            NWScript.sendMessageToPC(oPC, "Only players who are suffering from an infection may be targeted with this item.");
            return;
        }

        if(!CustomEffectSystem.DoesPCHaveCustomEffect(target, CustomEffectType.InfectionOverTime))
        {
            NWScript.sendMessageToPC(oPC, "Your target is not suffering from an infection.");
            return;
        }
        float distance = NWScript.getDistanceBetween(oPC, target);
        if(distance > 3.5f)
        {
            NWScript.sendMessageToPC(oPC, "Your target is too far away.");
            return;
        }

        final NWObject item = NWScript.getItemActivated();
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);
        final float delay = 7.0f - (skill * 0.5f);

        NWScript.sendMessageToPC(oPC, "You begin administering antibiotics to " + NWScript.getName(target, false) + ".");

        if(!oPC.equals(target))
            NWScript.sendMessageToPC(target, NWScript.getName(oPC, false) + " begins administering antibiotics to you..");

        NWNX_Player.StartGuiTimingBar(oPC, (int) delay, "");

        Scheduler.assign(oPC, () -> {
            pcGO.setIsBusy(true);

            NWScript.setFacingPoint(NWScript.getPosition(target));
            NWScript.actionPlayAnimation(Animation.LOOPING_GET_MID, 1.0f, delay);
            NWScript.setCommandable(false, oPC);
        });

        Scheduler.delay(oPC, (int) (delay * 1000), () -> {
            float distance1 = NWScript.getDistanceBetween(oPC, target);
            NWScript.setCommandable(true, oPC);
            pcGO.setIsBusy(false);

            if(distance1 > 3.5f)
            {
                NWScript.sendMessageToPC(oPC, "Your target is too far away.");
                return;
            }

            CustomEffectSystem.RemovePCCustomEffect(target, CustomEffectType.InfectionOverTime);
            ItemHelper.ReduceItemStack(item);

            NWScript.sendMessageToPC(oPC, "You successfully administer antibiotics to " + NWScript.getName(target, false) + ".");
        });
    }
}
