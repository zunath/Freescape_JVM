package GameSystems;

import Bioware.AddItemPropertyPolicy;
import Bioware.XP2;
import Common.Constants;
import Enumerations.CustomItemProperty;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWItemProperty;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.IpConstOnhitCastspell;
import org.nwnx.nwnx2.jvm.constants.ItemProperty;

public class ArmorSystem {

    public static void OnModuleEquipItem()
    {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        NWObject oItem = NWScript.getPCItemLastEquipped();

        ApplyOnHitCastSpellItemProperty(oItem);
        ApplyArmorBaseAC(oPC, null);
    }

    public static void OnModuleUnequipItem()
    {
        NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        NWObject oItem = NWScript.getPCItemLastUnequipped();
        ApplyArmorBaseAC(oPC, oItem);
    }

    private static void ApplyOnHitCastSpellItemProperty(NWObject oItem)
    {
        int baseItemType = NWScript.getBaseItemType(oItem);
        // Apply OnHitCastSpell property if it doesn't exist.
        if(baseItemType == BaseItem.ARMOR)
        {
            for(NWItemProperty ip : NWScript.getItemProperties(oItem))
            {
                if(NWScript.getItemPropertyType(ip) == ItemProperty.ONHITCASTSPELL)
                {
                    if(NWScript.getItemPropertySubType(ip) == IpConstOnhitCastspell.ONHIT_UNIQUEPOWER)
                    {
                        return;
                    }
                }
            }

            // No item property found. Add it to the armor.
            XP2.IPSafeAddItemProperty(oItem, NWScript.itemPropertyOnHitCastSpell(IpConstOnhitCastspell.ONHIT_UNIQUEPOWER, 40), 0.0f, AddItemPropertyPolicy.ReplaceExisting, false, false);
        }
    }

    private static void ApplyArmorBaseAC(NWObject oPC, NWObject itemUnequipped)
    {
        int ac = 0;
        for(int slot = 0; slot < Constants.NumberOfInventorySlots; slot++)
        {
            NWObject oItem = NWScript.getItemInSlot(slot, oPC);
            if(oItem.equals(itemUnequipped))
                continue;

            if(!oItem.equals(NWObject.INVALID))
            {
                int itemAC = 0;
                for(NWItemProperty ip : NWScript.getItemProperties(oItem))
                {
                    if(NWScript.getItemPropertyType(ip) == CustomItemProperty.AC)
                    {
                        int count = NWScript.getItemPropertyCostTableValue(ip);
                        if(count > itemAC)
                        {
                            itemAC = count;
                        }
                    }
                }

                ac += itemAC;
            }
        }

        NWNX_Creature.SetBaseAC(oPC, ac);
    }

}
