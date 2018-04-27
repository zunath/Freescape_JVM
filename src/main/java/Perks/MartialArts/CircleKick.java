package Perks.MartialArts;

import Enumerations.CustomItemType;
import GameObject.ItemGO;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class CircleKick implements IPerk {
    @Override
    public boolean CanCastSpell(NWObject oPC, NWObject oTarget) {
        return false;
    }

    @Override
    public String CannotCastSpellMessage() {
        return null;
    }

    @Override
    public int ManaCost(NWObject oPC, int baseManaCost) {
        return baseManaCost;
    }

    @Override
    public float CastingTime(NWObject oPC, float baseCastingTime) {
        return baseCastingTime;
    }

    @Override
    public float CooldownTime(NWObject oPC, float baseCooldownTime) {
        return baseCooldownTime;
    }

    @Override
    public void OnImpact(NWObject oPC, NWObject oTarget) {

    }

    @Override
    public void OnPurchased(NWObject oPC, int newLevel) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnRemoved(NWObject oPC) {
        NWNX_Creature.RemoveFeat(oPC, Feat.CIRCLE_KICK);
    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, oItem);
    }

    private void ApplyFeatChanges(NWObject oPC, NWObject unequippingItem)
    {
        NWObject mainHand = NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC);
        NWObject offHand = NWScript.getItemInSlot(InventorySlot.LEFTHAND, oPC);
        ItemGO mainGO = new ItemGO(mainHand);
        ItemGO offGO = new ItemGO(offHand);
        int mainType = mainGO.getCustomItemType();
        int offType = offGO.getCustomItemType();
        boolean receivesFeat = true;

        if(unequippingItem != null && unequippingItem.equals(mainHand))
        {
            mainHand = NWObject.INVALID;
        }
        else if(unequippingItem != null && unequippingItem.equals(offHand))
        {
            offHand = NWObject.INVALID;
        }

        if((!NWScript.getIsObjectValid(mainHand) && !NWScript.getIsObjectValid(offHand)) ||
                (mainType != CustomItemType.MartialArtWeapon || offType != CustomItemType.MartialArtWeapon))
        {
            receivesFeat = false;
        }

        if(receivesFeat)
        {
            NWNX_Creature.AddFeat(oPC, Feat.CIRCLE_KICK);
        }
        else
        {
            NWNX_Creature.RemoveFeat(oPC, Feat.CIRCLE_KICK);
        }
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
