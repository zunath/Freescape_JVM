package Perks.Archery;

import Enumerations.CustomItemType;
import GameObject.ItemGO;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.BaseItem;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class PointBlankShot implements IPerk {
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
        NWNX_Creature.RemoveFeat(oPC, Feat.POINT_BLANK_SHOT);
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
        NWObject armor = oItem == null ? NWScript.getItemInSlot(InventorySlot.CHEST, oPC) : oItem;
        if(NWScript.getBaseItemType(armor) != BaseItem.ARMOR) return;

        ItemGO itemGO = new ItemGO(armor);
        if(armor.equals(oItem) || itemGO.getCustomItemType() != CustomItemType.LightArmor)
        {
            NWNX_Creature.RemoveFeat(oPC, Feat.POINT_BLANK_SHOT);
            return;
        }

        if(!NWNX_Creature.GetKnowsFeat(oPC, Feat.POINT_BLANK_SHOT))
        {
            NWNX_Creature.AddFeat(oPC, Feat.POINT_BLANK_SHOT);
        }

    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
