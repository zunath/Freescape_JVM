package Item.Medical;

import GameObject.PlayerGO;
import Helper.ItemHelper;
import Common.IScriptEventHandler;
import GameSystems.ProgressionSystem;
import NWNX.NWNX_Player;
import org.nwnx.nwnx2.jvm.NWEffect;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.Animation;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.EffectType;

@SuppressWarnings("unused")
public class Stimulant implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {
        if(NWScript.getIsDM(oPC) || !NWScript.getIsPC(oPC)) return;

        final NWObject item = NWScript.getItemActivated();
        final int attribute = NWScript.getLocalInt(item, "STIMULANT_TYPE");
        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);
        final float duration = 60.0f + (skill * 6.0f);
        final int power = 1 + NWScript.getLocalInt(item, "STIMULANT_POWER");
        final float delay = 8.0f - (skill * 0.5f);
        final PlayerGO pcGO = new PlayerGO(oPC);

        if(pcGO.isBusy())
        {
            NWScript.sendMessageToPC(oPC, "You are busy.");
            return;
        }


        NWNX_Player.StartGuiTimingBar(oPC, (int) delay, "");

        Scheduler.assign(oPC, () -> {
            pcGO.setIsBusy(true);
            NWScript.actionPlayAnimation(Animation.LOOPING_GET_MID, 1.0f, delay);
            NWScript.setCommandable(false, oPC);
        });

        Scheduler.delay(oPC, (int) (delay * 1000), () -> {
            pcGO.setIsBusy(false);
            NWScript.setCommandable(true, oPC);
            pcGO.removeEffect(EffectType.ABILITY_INCREASE);
            NWEffect effect = NWScript.effectAbilityIncrease(attribute, power);
            NWScript.applyEffectToObject(DurationType.TEMPORARY, effect, oPC, duration);

            ItemHelper.ReduceItemStack(item);
        });


    }
}
