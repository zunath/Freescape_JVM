package Feat;

import Helper.ColorToken;
import Common.IScriptEventHandler;
import GameSystems.CombatSystem;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Action;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

@SuppressWarnings("UnusedDeclaration")
public class ReloadGun implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        CombatSystem combatSystem = new CombatSystem();

        if(NWScript.getCurrentHitPoints(oPC) < 1 || NWScript.getCurrentAction(oPC) == Action.SIT)
        {
            NWScript.sendMessageToPC(oPC, ColorToken.Red() + "You cannot do that at this time." + ColorToken.End());
        }
        else
        {
            NWObject oRightHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
            NWObject oLeftHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
            boolean bDualWield = false;

            if(!oLeftHand.equals(NWObject.INVALID)) bDualWield = true;

            combatSystem.ReloadAmmo(oPC, oRightHand, bDualWield);
        }
    }
}
