package Perks.Armor;

import Enumerations.CustomItemType;
import GameObject.ItemGO;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class Expertise implements IPerk {
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

    }

    @Override
    public void OnRemoved(NWObject oPC) {
        NWNX_Creature.RemoveFeat(oPC, Feat.EXPERTISE);
    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, oItem);
    }

    private void ApplyFeatChanges(NWObject oPC, NWObject oItem)
    {
        NWObject equipped = oItem == null ? NWScript.getItemInSlot(InventorySlot.CHEST, oPC) : oItem;
        ItemGO itemGO = new ItemGO(equipped);

        if(equipped.equals(oItem) || itemGO.getCustomItemType() != CustomItemType.HeavyArmor)
        {
            NWNX_Creature.RemoveFeat(oPC, Feat.EXPERTISE);
            return;
        }

        NWNX_Creature.AddFeat(oPC, Feat.EXPERTISE);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
