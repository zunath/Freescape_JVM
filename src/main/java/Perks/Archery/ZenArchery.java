package Perks.Archery;

import Enumerations.CustomItemType;
import GameObject.ItemGO;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class ZenArchery implements IPerk {
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
        NWNX_Creature.RemoveFeat(oPC, Feat.ZEN_ARCHERY);
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
        NWObject equipped = oItem == null ? NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC) : oItem;
        ItemGO itemGO = new ItemGO(equipped);

        if(equipped.equals(oItem) || (itemGO.getCustomItemType() != CustomItemType.Bow && itemGO.getCustomItemType() != CustomItemType.Crossbow))
        {
            NWNX_Creature.RemoveFeat(oPC, Feat.ZEN_ARCHERY);
            return;
        }

        NWNX_Creature.AddFeat(oPC, Feat.ZEN_ARCHERY);
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
