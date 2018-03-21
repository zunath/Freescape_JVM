package Placeable.Resource;

import Common.IScriptEventHandler;
import GameObject.ItemGO;
import Helper.ColorToken;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

import java.util.concurrent.ThreadLocalRandom;

public class OnAttacked implements IScriptEventHandler {
    @Override
    public void runScript(final NWObject objSelf) {
        NWObject oPC = NWScript.getLastAttacker(objSelf);
        NWObject oWeapon = NWScript.getLastWeaponUsed(oPC);
        Integer type = NWScript.getBaseItemType(oWeapon);
        int resourceCount = NWScript.getLocalInt(objSelf, "RESOURCE_COUNT");

        if(resourceCount <= -1)
        {
            resourceCount = ThreadLocalRandom.current().nextInt(3) + 3;
            NWScript.setLocalInt(objSelf, "RESOURCE_COUNT", resourceCount);
        }

        if(type == BaseItem.INVALID)
        {
            oWeapon = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
        }

        int activityID = NWScript.getLocalInt(objSelf, "RESOURCE_ACTIVITY");
        ItemGO weaponGO = new ItemGO(oWeapon);

        String improperWeaponMessage = "";
        boolean usingCorrectWeapon = true;
        if(activityID == 1) // 1 = Logging
        {
            usingCorrectWeapon = weaponGO.IsBlade() || weaponGO.IsTwinBlade() || weaponGO.IsHeavyBlade() || weaponGO.IsFinesseBlade() || weaponGO.IsPolearm();
            improperWeaponMessage = "You must be using a blade to harvest this object.";
        }
        else if(activityID == 2) // Mining
        {
            usingCorrectWeapon = weaponGO.IsBlade() || weaponGO.IsTwinBlade() || weaponGO.IsHeavyBlade() || weaponGO.IsFinesseBlade() || weaponGO.IsPolearm() ||
                                 weaponGO.IsBlunt() || weaponGO.IsHeavyBlunt();
            improperWeaponMessage = "You must be using a blade or a blunt weapon to harvest this object.";
        }

        if(!usingCorrectWeapon)
        {
            NWScript.setPlotFlag(objSelf, true);
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + improperWeaponMessage + ColorToken.End());
            NWScript.setLocalInt(oPC, "NOT_USING_CORRECT_WEAPON", 1);
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));

            Scheduler.delay(oPC, 1000, () -> NWScript.setPlotFlag(objSelf, false));
        }

    }
}
