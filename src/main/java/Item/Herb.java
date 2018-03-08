package Item;

import Common.IScriptEventHandler;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.DurationType;
import org.nwnx.nwnx2.jvm.constants.Vfx;

import java.util.Objects;

public class Herb implements IScriptEventHandler {
    @Override
    public void runScript(NWObject objSelf) {

        ProgressionSystem progressionSystem = new ProgressionSystem();
        NWObject oPC = NWScript.getItemActivator();
        NWObject oTarget = NWScript.getItemActivatedTarget();
        NWObject oItem = NWScript.getItemActivated();
        String sResref = NWScript.getResRef(oItem);
        float fRecovery = 0.0f;
        boolean bRemovePoison = false;
        int iSkill = progressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_FIRST_AID);

        // Green Herb
        if(Objects.equals(sResref, "green_herb"))
        {
            fRecovery = 0.25f;
        }
        // Blue Herb
        else if(Objects.equals(sResref, "blue_herb"))
        {
            bRemovePoison = true;
        }
        // Green + Red
        else if(Objects.equals(sResref, "gr_herb"))
        {
            fRecovery = 0.80f;
        }
        // Green + Green
        else if(Objects.equals(sResref, "gg_herb"))
        {
            fRecovery = 0.55f;
        }
        // Green + Green + Green
        else if(Objects.equals(sResref, "ggg_herb"))
        {
            fRecovery = 0.75f;
        }
        // Green + Red + Blue
        else if(Objects.equals(sResref, "grb_herb"))
        {
            fRecovery = 0.80f;
            bRemovePoison = true;
        }
        // Green + Blue
        else if(Objects.equals(sResref, "gb_herb"))
        {
            fRecovery = 0.25f;
            bRemovePoison = true;
        }

        // First Aid Spray
        else if(Objects.equals(sResref, "first_aid_spray"))
        {
            fRecovery = 100.0f;
        }

        // Herb removes poison - remove it!
        if(bRemovePoison)
        {
        }

        // Herb restores HP - heal PC!
        if(fRecovery > 0.0)
        {
            // First Aid skill grants +1% to recovery per level
            if(iSkill > 0 && fRecovery < 100.0)
            {
                fRecovery = fRecovery + (fRecovery * 0.01f);
            }

            int iRecovery = (int)(NWScript.getMaxHitPoints(oTarget) * fRecovery);
            NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectHeal(iRecovery), oTarget, 0.0f);
        }
        NWScript.applyEffectToObject(DurationType.INSTANT, NWScript.effectVisualEffect(Vfx.IMP_REMOVE_CONDITION, false), oTarget, 0.0f);

        NWScript.destroyObject(oItem, 0.0f);
    }
}
