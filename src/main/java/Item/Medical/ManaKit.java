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
public class ManaKit implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {
        final NWObject oTarget = NWScript.getItemActivatedTarget();
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

        float distance = NWScript.getDistanceBetween(oPC, oTarget);
        if(distance > 3.5f)
        {
            NWScript.sendMessageToPC(oPC, "Your target is out of range.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);
        final int durationTicks = 4 + (skill / 2);
        final float delay = 12.0f - (skill * 0.5f);
        final NWObject item = NWScript.getItemActivated();

        NWNX_Player.StartGuiTimingBar(oPC, (int) delay, "");
        NWScript.sendMessageToPC(oPC, "You begin applying a mana kit to " + NWScript.getName(oTarget, false) + ".");
        if(!oPC.equals(oTarget))
            NWScript.sendMessageToPC(oTarget, NWScript.getName(oPC, false) + " begins applying a mana kit to you.");

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
            CustomEffectSystem.ApplyCustomEffect(oPC, oTarget, CustomEffectType.ManaKit, durationTicks);
            ItemHelper.ReduceItemStack(item);
            NWScript.sendMessageToPC(oPC, "You successfully apply a mana kit to " + NWScript.getName(oTarget, false) + ".");
        });
    }
}
