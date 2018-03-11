package Item.Repair;

import Common.IScriptEventHandler;
import Enumerations.SkillID;
import GameSystems.DurabilitySystem;
import GameSystems.SkillSystem;
import Helper.ItemHelper;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

@SuppressWarnings("unused")
public class BladeRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject target = NWScript.getItemActivatedTarget();
        NWObject item = NWScript.getItemActivated();

        if(!ItemHelper.IsBlade(target))
        {
            NWScript.sendMessageToPC(oPC, "You cannot repair that item with this kit.");
            return;
        }

        int skill = SkillSystem.GetPCSkill(oPC, SkillID.ItemRepair).getRank();
        float repairAmount = 10.0f + skill * 0.2f;

        DurabilitySystem.RunItemRepair(oPC, item, repairAmount);

        NWScript.destroyObject(item, 0.0f);
    }

}
