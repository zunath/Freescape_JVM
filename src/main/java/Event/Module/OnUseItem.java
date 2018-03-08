package Event.Module;

import Bioware.XP2;
import Common.IScriptEventHandler;
import Helper.ScriptHelper;
import NWNX.NWNX_Events;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.Scheduler;
import org.nwnx.nwnx2.jvm.constants.IpConst;

@SuppressWarnings("UnusedDeclaration")
public class OnUseItem implements IScriptEventHandler {
    @Override
    public void runScript(NWObject oPC) {
        HandleSpecificItemUses(oPC);
    }

    private void HandleSpecificItemUses(NWObject oPC)
    {
        NWObject oItem = NWNX_Events.OnItemUsed_GetItem();
        String sTag = NWScript.getTag(oItem);
        int iSubtype = NWNX_Events.OnItemUsed_GetItemPropertyIndex();

        // Change Ammo Priority Property
        boolean bAmmoPriority = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(548, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Change Firing Mode Property
        boolean bChangeFiringMode = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(546, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Combine Property
        boolean bCombine = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(547, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Unique Power: Self Only Property
        boolean bActivateSelf = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(335, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Toggle Radio Power Property
        boolean bRadioPower = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(549, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Change Radio Channel Property
        boolean bRadioChannel = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(550, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Use UseLockpick Property
        boolean bLockpick = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(551, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Use Structure Tool Property
        boolean bUseStructure = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(553, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Open Rest Menu Property
        boolean bOpenRestMenu = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(554, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);
        // Check Infection Level Property
        boolean bCheckInfectionLevel = XP2.IPGetItemHasProperty(oItem, NWScript.itemPropertyCastSpell(556, IpConst.CASTSPELL_NUMUSES_UNLIMITED_USE), -1, false);

        boolean bBypassEvent = true;

        // Firearms - Ammo Priority (0), Change Firing Mode (1), Combine (2)
        if(bAmmoPriority && bCombine && bChangeFiringMode)
        {
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunAmmo");
            }
            else if(iSubtype == 1)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunMode");
            }
            else if(iSubtype == 2)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.CombineItem");
            }
        }
        // Firearms - Ammo Priority (0), Change Firing Mode (1)
        else if(bAmmoPriority && bChangeFiringMode)
        {
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunAmmo");
            }
            else if(iSubtype == 1)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunMode");
            }
        }
        // Firearms - Ammo Priority (0), Combine (1)
        else if(bAmmoPriority && bCombine)
        {
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunAmmo");
            }
            else if(iSubtype == 1)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.CombineItem");
            }
        }
        // Firearms - Ammo Priority (0)
        else if(bAmmoPriority)
        {
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.ChangeGunAmmo");
            }
        }

        // Unique Power Self Only and Combine
        else if(bCombine && bActivateSelf)
        {
            // Combine
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.CombineItem");
            }
            else if(iSubtype == 1)
            {
                bBypassEvent = false;
            }
        }

        // Combine (0)
        else if(bCombine)
        {
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.CombineItem");
            }
        }

        // Change Radio Channel (0) and Toggle Radio Power (1)
        else if(bRadioPower && bRadioChannel)
        {
            bBypassEvent = true;
            // Change Radio Channel
            if(iSubtype == 0)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.Radio_ChangeChannel");
            }
            // Toggle Radio Power
            else if(iSubtype == 1)
            {
                ScriptHelper.RunJavaScript(oPC, "Feat.Radio_TogglePower");
            }
        }
        // Fire tag based scripting in all other cases (I.E: Don't bypass this event)
        else
        {
            bBypassEvent = false;
        }

        // The entirety of the OnActivateItem will be skipped if bBypassEvent is true.
        if(bBypassEvent)
        {
            Scheduler.assign(oPC, () -> NWScript.clearAllActions(false));
        }
    }

}
