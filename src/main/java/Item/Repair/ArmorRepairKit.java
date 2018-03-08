package Item.Repair;

import Common.IScriptEventHandler;
import Enumerations.AbilityType;
import GameSystems.DurabilitySystem;
import GameSystems.MagicSystem;
import GameSystems.ProgressionSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class ArmorRepairKit implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        NWObject target = NWScript.getItemActivatedTarget();
        NWObject item = NWScript.getItemActivated();
        int targetType = NWScript.getBaseItemType(target);

        ArrayList<Integer> allowedTypes = new ArrayList<>();
        Collections.addAll(allowedTypes, BaseItem.BELT, BaseItem.ARMOR, BaseItem.GLOVES, BaseItem.BRACER, BaseItem.HELMET);

        if(!allowedTypes.contains(targetType))
        {
            NWScript.sendMessageToPC(oPC, "You cannot repair that item with this kit.");
            return;
        }

        int skill = ProgressionSystem.GetPlayerSkillLevel(oPC, ProgressionSystem.SkillType_ITEM_REPAIR) * 2;
        float repairAmount = 10.0f + skill;

        if(MagicSystem.IsAbilityEquipped(oPC, AbilityType.Fixer))
        {
            repairAmount *= 1.25f;
        }

        DurabilitySystem.RunItemRepair(oPC, item, repairAmount);

        NWScript.destroyObject(item, 0.0f);
    }
}
