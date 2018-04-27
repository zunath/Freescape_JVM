package Perks.TwinBlade;

import Enumerations.CustomItemType;
import Enumerations.PerkID;
import GameObject.ItemGO;
import GameSystems.PerkSystem;
import NWNX.NWNX_Creature;
import Perks.IPerk;
import org.nwnx.nwnx2.jvm.NWObject;
import org.nwnx.nwnx2.jvm.NWScript;
import org.nwnx.nwnx2.jvm.constants.Feat;
import org.nwnx.nwnx2.jvm.constants.InventorySlot;

public class TwinBladeMastery implements IPerk {
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
        RemoveFeats(oPC);
    }

    @Override
    public void OnItemEquipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, null);
    }

    @Override
    public void OnItemUnequipped(NWObject oPC, NWObject oItem) {
        ApplyFeatChanges(oPC, oItem);
    }

    private void RemoveFeats(NWObject oPC)
    {
        NWNX_Creature.RemoveFeat(oPC, Feat.TWO_WEAPON_FIGHTING);
        NWNX_Creature.RemoveFeat(oPC, Feat.AMBIDEXTERITY);
        NWNX_Creature.RemoveFeat(oPC, Feat.IMPROVED_TWO_WEAPON_FIGHTING);
    }

    private void ApplyFeatChanges(NWObject oPC, NWObject oItem)
    {
        NWObject equipped = oItem == null ? NWScript.getItemInSlot(InventorySlot.RIGHTHAND, oPC) : oItem;
        ItemGO itemGO = new ItemGO(equipped);

        if(equipped.equals(oItem) || itemGO.getCustomItemType() != CustomItemType.TwinBlade)
        {
            RemoveFeats(oPC);
            return;
        }

        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.TwinBladeMastery);
        NWNX_Creature.AddFeat(oPC, Feat.TWO_WEAPON_FIGHTING);

        if(perkLevel >= 2)
        {
            NWNX_Creature.AddFeat(oPC, Feat.AMBIDEXTERITY);
        }
        if(perkLevel >= 3)
        {
            NWNX_Creature.AddFeat(oPC, Feat.IMPROVED_TWO_WEAPON_FIGHTING);
        }
    }

    @Override
    public boolean IsHostile() {
        return false;
    }
}
