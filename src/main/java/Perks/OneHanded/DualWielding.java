package Perks.OneHanded;

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

import static org.nwnx.nwnx2.jvm.NWScript.*;

public class DualWielding implements IPerk {
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
        NWObject mainEquipped = oItem == null ? getItemInSlot(InventorySlot.RIGHTHAND, oPC) : oItem;
        NWObject offEquipped = oItem == null ? getItemInSlot(InventorySlot.LEFTHAND, oPC) : oItem;
        ItemGO mainGO = new ItemGO(mainEquipped);
        ItemGO offGO = new ItemGO(offEquipped);

        // oItem was unequipped.
        if(mainEquipped.equals(oItem) || offEquipped.equals(oItem))
        {
            RemoveFeats(oPC);
            return;
        }

        // Main or offhand was invalid (i.e not equipped)
        if(!getIsObjectValid(mainEquipped) || !getIsObjectValid(offEquipped))
        {
            RemoveFeats(oPC);
            return;
        }

        // Main or offhand is not acceptable item type.
        if((!mainGO.IsBlade() || !mainGO.IsBlunt() || !mainGO.IsFinesseBlade()) ||
                (!offGO.IsBlade() || !offGO.IsBlunt() || !offGO.IsFinesseBlade()))
        {
            RemoveFeats(oPC);
            return;
        }


        int perkLevel = PerkSystem.GetPCPerkLevel(oPC, PerkID.DualWielding);
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
