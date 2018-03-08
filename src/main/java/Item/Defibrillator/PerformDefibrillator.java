package Item.Defibrillator;

import Helper.ColorToken;
import Common.IScriptEventHandler;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.DurationType;

public class PerformDefibrillator implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject oPC) {

        ProgressionSystem progressionSystem = new ProgressionSystem();
        NWObject oTarget = NWScript.getLocalObject(oPC, "DEFIB_TEMP_TARGET_OBJECT");
        NWObject oItem = NWScript.getItemPossessedBy(oPC, "item_defib");
        float fSkillBonus = progressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID) * 0.05f;
        float fHPRecoverPercentage = 0.0f + fSkillBonus;
        int iRecoverAmount = (int)(NWScript.getMaxHitPoints(oTarget) * fHPRecoverPercentage);

        // Possible that PC left server or player canceled the action.
        if(!NWScript.getIsObjectValid(oTarget) || !NWScript.getIsPC(oTarget) || NWScript.getCurrentHitPoints(oTarget) > 0)
        {
            return;
        }

        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectResurrection(), oTarget, 0.0f);
        if(iRecoverAmount > 0)
        {
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(iRecoverAmount), oTarget, 0.0f);
        }

        NWScript.applyEffectToObject(DurationType.TEMPORARY, NWScript.effectSlow(), oTarget, 120.0f - (5.0f * fSkillBonus));
        NWScript.floatingTextStringOnCreature(ColorToken.Purple() + "**SUCCESS** Target has been revived!." + ColorToken.End(), oPC, false);

        NWScript.deleteLocalObject(oPC, "DEFIB_TEMP_TARGET_OBJECT");
        NWScript.destroyObject(oItem, 0.0f);

        Scheduler.assign(oPC, () -> NWScript.setCommandable(true, oPC));
    }
}
