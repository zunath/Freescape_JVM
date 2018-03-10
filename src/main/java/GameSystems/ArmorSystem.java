package GameSystems;

import Common.Constants;
import GameObject.ItemGO;
import NWNX.NWNX_Creature;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;

public class ArmorSystem {

    public static void OnModuleEquipItem()
    {
        NWObject oPC = NWScript.getPCItemLastEquippedBy();
        ApplyArmorBaseAC(oPC, null);
    }

    public static void OnModuleUnequipItem()
    {
        NWObject oPC = NWScript.getPCItemLastUnequippedBy();
        NWObject oItem = NWScript.getPCItemLastUnequipped();
        ApplyArmorBaseAC(oPC, oItem);
    }

    private static void ApplyArmorBaseAC(NWObject oPC, NWObject itemUnequipped)
    {
        int ac = 0;
        for(int slot = 0; slot < Constants.NumberOfInventorySlots; slot++)
        {
            NWObject oItem = NWScript.getItemInSlot(slot, oPC);
            if(oItem.equals(itemUnequipped))
                continue;

            ItemGO itemGO = new ItemGO(oItem);
            if(!oItem.equals(NWObject.INVALID))
            {
                int itemAC = itemGO.getAC();

                ac += itemAC;
            }
        }

        NWNX_Creature.SetBaseAC(oPC, ac);
    }

}
